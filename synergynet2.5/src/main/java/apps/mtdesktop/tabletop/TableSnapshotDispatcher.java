package apps.mtdesktop.tabletop;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import apps.mtdesktop.MTDesktopConfigurations;

import com.jme.image.Image;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;



public class TableSnapshotDispatcher {
	
	private AffineTransform at;
	private BufferedImage img;
	private BufferedImage dest;
	private Graphics2D g;
	
	private final int width;
	private final int height;
    private final ByteBuffer buff;
	
	private float framerate;
	private float snapshotDelay = 1f;
	private boolean dispatchSnapshotEnabled = false;
	
	public TableSnapshotDispatcher(){
		width = DisplaySystem.getDisplaySystem().getWidth();
		height = DisplaySystem.getDisplaySystem().getHeight();
		buff = BufferUtils.createByteBuffer(width * height * 3);
		
		at = AffineTransform.getScaleInstance(MTDesktopConfigurations.defaultRadarImageScale,MTDesktopConfigurations.defaultRadarImageScale);
    	img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		dest = new BufferedImage((int)(width * MTDesktopConfigurations.defaultRadarImageScale),(int)(height * MTDesktopConfigurations.defaultRadarImageScale),BufferedImage.TYPE_INT_RGB);
		g = dest.createGraphics();
	}
	
	public void update(float tpf){
		if((framerate - tpf) > 0){
			framerate-= tpf;
			return;
		}
		framerate = snapshotDelay;
		
		if(dispatchSnapshotEnabled){		
			try{
				if(DisplaySystem.getDisplaySystem().getRenderer() != null){
					DisplaySystem.getDisplaySystem().getRenderer().grabScreenContents(buff, Image.Format.RGB4, 0, 0, width, height);
			        final int w = width;
			        final int h = height;
			        Thread saveThread = new Thread() {
			            
			            public void run() {
			            	try{
				                for (int x = 0; x < w; x++) {
				                    for (int y = 0; y < h; y++) {
				                        
				                        int index = 3 * ((h- y - 1) * w + x);
				                        int argb = (((int) (buff.get(index+0)) & 0xFF) << 16) //r
				                                 | (((int) (buff.get(index+1)) & 0xFF) << 8)  //g
				                                 | (((int) (buff.get(index+2)) & 0xFF));      //b
	
				                        img.setRGB(x, y, argb);
				                    }
				                }
								g.drawRenderedImage(img,at);
								ImageIO.write( dest, "gif", new File(MTDesktopConfigurations.OutboxFolder + "/snapShotTabletop.gif"));
			            	}catch(Exception exp){
			            		exp.printStackTrace();
			            	}
							buff.clear();
			            }
			        };
			        saveThread.start();
				}
	
			}catch(Exception exp){
				exp.printStackTrace();
			}
		}

	}
	
	protected void enable(boolean isEnabled){
		this.dispatchSnapshotEnabled = isEnabled;
	}

}