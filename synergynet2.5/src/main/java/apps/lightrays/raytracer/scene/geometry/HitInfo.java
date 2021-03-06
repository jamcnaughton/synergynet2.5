package apps.lightrays.raytracer.scene.geometry;

import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.Vector;

/**
 * The Class HitInfo.
 */
public class HitInfo {
	
	/** The distance. */
	double distance; // Distance to hit (used & reset by Intersect).
	
	/** The ignore. */
	Object ignore; // One object to ignore (used by Cast).
	
	/** The normal. */
	Vector normal; // Surface normal (set by Intersect).
	
	/** The object. */
	Object object; // The object that was hit (set by Intersect).
	
	/** The point. */
	Vector point; // ray-object intersection point (set by Intersect).
	
	/** The ray. */
	Ray ray; // The ray that hit the surface (set by Intersect).
	
	/** The uv. */
	Vector uv; // Texture coordinates (set by intersect).
	
}
