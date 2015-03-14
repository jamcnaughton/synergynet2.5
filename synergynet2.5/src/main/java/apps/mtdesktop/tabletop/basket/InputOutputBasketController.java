package apps.mtdesktop.tabletop.basket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import apps.mtdesktop.tabletop.MTTableClient;
import apps.mtdesktop.tabletop.TabletopContentManager;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import core.SynergyNetDesktop;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.HtmlFrame;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

public class InputOutputBasketController implements IMultiTouchEventListener{
	
	private IJMEMultiTouchPicker pickSystem;
	protected BasketManager basketManager;
	protected Node orthoNode;
	private ConcurrentLinkedQueue<ItemToBasket> sendQueue = new ConcurrentLinkedQueue<ItemToBasket>();
	private ConcurrentLinkedQueue<ItemToBasket> deleteQueue = new ConcurrentLinkedQueue<ItemToBasket>();
	private ConcurrentLinkedQueue<ItemFlick> receiveQueue = new ConcurrentLinkedQueue<ItemFlick>();

	
	public InputOutputBasketController(BasketManager basketManager, DefaultSynergyNetApp app){
		this.basketManager = basketManager;
		this.pickSystem = MultiTouchInputFilterManager.getInstance().getPickingSystem();
		orthoNode = app.getOrthoNode();
	}
	
	public void remoteItemReceived(ContentItem item){
		receiveQueue.add(new ItemFlick(item));
	}
	
	public void update(float tpf){
		
			// process send queue
			Iterator<ItemToBasket> iter = sendQueue.iterator();
			while(iter.hasNext()){
				ItemToBasket i = iter.next();
				((OrthoContentItem)i.item).setRotateTranslateScalable(false);
				if(i.fadeDelay <0){
					if(i.item.getScale() <0.01f){
						i.item.setVisible(false);
						i.item.setScale(i.currentScale);
						basketManager.sendItemToRemoteBasket(i.item, i.basket.getTableId());
						if(i.basket != null)
							i.basket.removeItem(i.item);
						else
							i.item.getContentSystem().removeContentItem(i.item);
						iter.remove();
					}else{
						i.item.setScale(i.item.getScale()-0.01f);
					}
					i.fadeDelay = 0.05f;
				}
				i.fadeDelay-=10 * tpf;
			}
			
			// process delete queue
			Iterator<ItemToBasket> iter2 = deleteQueue.iterator();
			while(iter2.hasNext()){
				ItemToBasket i = iter2.next();
				((OrthoContentItem)i.item).setRotateTranslateScalable(false);
				if(i.fadeDelay <0){
					if(i.item.getScale() <0.01f){
						if(i.basket != null)
							i.basket.removeItem(i.item);
						else
							i.item.getContentSystem().removeContentItem(i.item);
						iter2.remove();
					}else{
						i.item.setScale(i.item.getScale()-0.01f);
					}
					i.fadeDelay = 0.05f;
				}
				i.fadeDelay-=10 * tpf;
			}	
		
			Iterator<ItemFlick> iter3 = receiveQueue.iterator();
			while(iter3.hasNext()){
				ItemFlick iFlick = iter3.next();
				if(iFlick.flickDelay <0){
					iFlick.item.setVisible(!iFlick.item.isVisible(), false);
					iFlick.flickDelay = 1;
				}
				iFlick.flickDelay-=2 * tpf;
			}
	}


	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		for(JmeNetworkedBasket basket: basketManager.getBaskets().values()) basket.getWindow().setAsBottomObject();
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		List<Spatial> spatials = getPickedSpatials(event.getCursorID(), new Vector2f(x, y));
		List<ContentItem> pickedItems = getPickedItems(spatials);
		for(ContentItem item: pickedItems){
			Iterator<ItemFlick> iter = receiveQueue.iterator();
			while(iter.hasNext())
				if(iter.next().item.getName().equals(item.getName())){
					item.setVisible(true);
					iter.remove();
					return;
				}
		}
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		List<Spatial> spatials = getPickedSpatials(event.getCursorID(), new Vector2f(x, y));
		JmeNetworkedBasket targetBasket = getPickedBasket(spatials);
		List<ContentItem> pickedItems = getPickedItems(spatials);
		for(ContentItem item: pickedItems){
			if(targetBasket != null && !targetBasket.isExcluded(item) && !(item.getName().equalsIgnoreCase(MTTableClient.background.getName()))){
				String pickedComponent = targetBasket.getComponentAt(x, y);
				if(pickedComponent.equalsIgnoreCase("basket") && !targetBasket.getWindow().getAllItemsIncludeSystemItems().contains(item)){
					//put item in basket
					item.setLocalLocation(targetBasket.getWindow().getLocation().x, targetBasket.getWindow().getLocation().y);
					sendQueue.add(new ItemToBasket(item, targetBasket, item.getScale()));
				}else if(pickedComponent.equalsIgnoreCase("copy")){
					try {
						ContentItem copy;
						ContentSystem consys = item.getContentSystem();
						if(item instanceof HtmlFrame){
							copy = (HtmlFrame) consys.createContentItem(HtmlFrame.class);
							((HtmlFrame)copy).setHtmlContent(((HtmlFrame)item).getHtmlContent());
							copy.setAngle(((HtmlFrame)item).getAngle());
							copy.setScale(((HtmlFrame)item).getScale());
						}else if(item instanceof MediaPlayer){
							copy = (MediaPlayer) consys.createContentItem(MediaPlayer.class);
							((MediaPlayer)copy).setMediaURL(((MediaPlayer)item).getMediaURL());
							copy.setAngle(((MediaPlayer)item).getAngle());
							copy.setScale(((MediaPlayer)item).getScale());
						}else{
							copy = (ContentItem)item.clone();
							copy.name = UUID.randomUUID().toString();
							consys.addContentItem(copy);
							copy.init();
						}
						((OrthoContentItem)copy).setRotateTranslateScalable(true);
						((OrthoContentItem)copy).setBringToTopable(true);
						((OrthoContentItem)copy).setAsTopObject();
						((OrthoContentItem)copy).setScaleLimit(TabletopContentManager.MIN_SCALE, TabletopContentManager.MAX_SCALE);
						((Spatial)((OrthoContentItem)copy).getImplementationObject()).updateModelBound();
						if(targetBasket.getWindow().getAllItemsIncludeSystemItems().contains(item))
							((OrthoContentItem)copy).centerItem();
						else
							copy.setLocalLocation(item.getLocalLocation().x + 50, item.getLocalLocation().y+50);
					} catch (CloneNotSupportedException e) {
						 
						e.printStackTrace();
					}
				}else if(pickedComponent.equalsIgnoreCase("bin")){
					if(!targetBasket.getWindow().getAllItemsIncludeSystemItems().contains(item))
						item.setLocalLocation(x, y);
					deleteQueue.add(new ItemToBasket(item, targetBasket, item.getScale()));
				}
				
			}else if(targetBasket == null){
				JmeNetworkedBasket containerBasket = getContainerBasketForItem(item);
				if(containerBasket != null){
					// take item out of basket
					containerBasket.detachItem(item);
					item.setLocalLocation(x, y);
					((OrthoContentItem)item).setOrder(999999);
					((OrthoContentItem)item).setScaleLimit(TabletopContentManager.MIN_SCALE, TabletopContentManager.MAX_SCALE);
					((Spatial)item.getImplementationObject()).updateRenderState();
				}
			}
		}
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}
	
	
	private JmeNetworkedBasket getPickedBasket(List<Spatial> spatials){
			for(JmeNetworkedBasket basket: basketManager.getBaskets().values()){
				for(Spatial spatial: spatials){
					if(basket.isBasketComponent(spatial.getName()))
						return basket;
				}
		}
		return null;
	}
	
	private List<ContentItem> getPickedItems(List<Spatial> spatials){
		List<ContentItem> items = new ArrayList<ContentItem>();
		for(Spatial spatial: spatials){
			if(!isBasket(spatial.getName())){
				ContentItem item = basketManager.getContentSystem().getContentItem(spatial.getName());
				items.add(item);
			}
		}
		return items;
	}
	
	private List<Spatial> getPickedSpatials(long id, Vector2f position)
	{
		PickRequest req = new PickRequest(id, position);
		List<PickResultData> pickResults;
		List<Spatial> pickedSpatials = new ArrayList<Spatial>();
		try {
				pickResults = pickSystem.doPick(req);
				for(PickResultData pr : pickResults) {
					Spatial targetSpatial = pr.getPickedSpatial();
					while(targetSpatial.getParent() != null && targetSpatial.getParent()!= orthoNode && !isBasket(targetSpatial.getParent().getName())){
						targetSpatial = targetSpatial.getParent();
					}
					pickedSpatials.add(targetSpatial);
				}
			}
		catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return pickedSpatials;
	}
	
	private boolean isBasket(String objectName) {
		for(JmeNetworkedBasket basket: basketManager.getBaskets().values()){
			if(basket.isBasketComponent(objectName))
				return true;
		}
		return false;
	}

	private JmeNetworkedBasket getContainerBasketForItem(ContentItem item){
		for(JmeNetworkedBasket basket: basketManager.getBaskets().values())
			if(basket.getWindow().getAllItemsIncludeSystemItems().contains(item)) return basket;
		return null;
	}
	
	class ItemToBasket{
		public ContentItem item;
		public JmeNetworkedBasket basket;
		public float currentScale;
		public float fadeDelay = 0.05f;
		
		public ItemToBasket(ContentItem item, JmeNetworkedBasket basket, float currentScale){
			this.item = item;
			this.basket = basket;
			this.currentScale = currentScale;
		}
	}
	
	class ItemFlick{
		public ContentItem item;
		public float flickDelay = 1;
		
		public ItemFlick(ContentItem item){
			this.item = item;
		}
	}
}
