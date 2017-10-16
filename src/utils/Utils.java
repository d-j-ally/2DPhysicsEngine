package utils;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import engine.Main;
import entity.Entity;
import entity.Player;
import gameobject.GameObject;

public final class Utils {
	
	private Utils() {
		
	}
	
	private static Random random = new Random();
	
	//generates an integer between 0 (inclusive) and maxBound (exclusive)
	public static int genRandomNumber(int maxBound) {
		return random.nextInt(maxBound);
	}
	
	public static void drawQuad(float x, float y, float z, int width, int height) {
		glDisable(GL_TEXTURE_2D);
		
		glBegin(GL_QUADS);
		glVertex3f(x, y, z);
		glVertex3f(x + width, y, z);
		glVertex3f(x + width, y + height, z);
		glVertex3f(x, y + height, z);
		glEnd();
		
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void drawSector(float x1, float y1, float z, float radius, float centreAngle, float width) 
	{
		float x2;
		float y2;
		
		glDisable(GL_TEXTURE_2D);
		
		glBegin(GL_TRIANGLE_FAN);
		
		glVertex3f(x1, y1, z);
		for (double angle = centreAngle - width / 2; angle < centreAngle + width / 2; angle += 0.2)
		{
			double anglerad = Math.toRadians(angle);
		    x2 = x1 + (float)Math.sin(anglerad) * radius;
		    y2 = y1 + (float)Math.cos(anglerad) * radius;
		    glVertex3f(x2, y2, z);
		}
		glEnd();

		glEnable(GL_TEXTURE_2D);
	}
	
	public static void drawQuadTex(Texture texture, float x, float y, float z, int width, int height) {
		texture.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //orary additions, supposed to reduce blurring
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); //orary additions, supposed to reduce blurring
		glTranslatef(x, y, z);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 1);
		glVertex2f(0, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, height);
		glTexCoord2f(0, 0);
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
	}
	
	public static Texture loadTexture(String path, String fileType) {
		Texture tex = null;
		InputStream in = ResourceLoader.getResourceAsStream(path);
		try {
			tex = TextureLoader.getTexture(fileType, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}
	
	public static Texture quickLoad(String name) {
		Texture tex = loadTexture("/res/sprites/" + name + ".png", "PNG");
		if ((tex.getImageWidth() & (tex.getImageWidth() - 1)) != 0 ||
			(tex.getImageHeight() & (tex.getImageHeight() - 1)) != 0) {
			System.out.println(name + " texture must have both its dimensions as a power of 2");
		}
		return tex;
	}
	

	
	public static float calculateXDistance(GameObject go1, GameObject go2) {
		return go2.x - go1.x;
	}
	
	public static float calculateYDistance(GameObject go1, GameObject go2) {
		return go2.y - go1.y;
	}
	
	public static float calculateDistanceFromCentre(GameObject go1, GameObject go2) {
		float go1X = go1.x + go1.width / 2;
		float go1Y = go1.y + go1.height / 2;
		float go2X = go2.x + go2.width / 2;
		float go2Y = go2.y + go2.height / 2;
		return (float)Math.sqrt(Math.pow(go2X - go1X, 2) + Math.pow(go2Y - go1Y, 2));
	}
	
	//Calculates the angle in degrees between an origin and a point relative to north, moving clockwise
	public static float calculateAngle(float originX, float originY, float pointX, float pointY) {
		float yDifference = pointY - originY;
		float xDifference = pointX - originX;
		
		float angle = (float)Math.toDegrees(Math.atan(-yDifference / xDifference));
		
		if (xDifference <= 0) {
			angle -= 90;
		} else {
			angle += 90;
		}
		
		return angle;
	}
	
	public static boolean bottomLeftCornerIntersects(GameObject go1, GameObject go2) {
		if (go1.x >= go2.x && go1.x <= go2.x + go2.width && go1.y >= go2.y && go1.y <= go2.y + go2.height) {
			return true;
		}
		return false;
	}
	
	public static boolean bottomRightCornerIntersects(GameObject go1, GameObject go2) {
		if (go1.x + go1.width >= go2.x && go1.x + go1.width <= go2.x + go2.width && go1.y >= go2.y && go1.y <= go2.y + go2.height) {
			return true;
		}
		return false;
	}
	
	public static boolean topLeftCornerIntersects(GameObject go1, GameObject go2) {
		if (go1.x >= go2.x && go1.x <= go2.x + go2.width && go1.y + go1.height >= go2.y && go1.y + go1.height <= go2.y + go2.height) {
			return true;
		}
		return false;
	}
	
	public static boolean topRightCornerIntersects(GameObject go1, GameObject go2) {
		if (go1.x + go1.width >= go2.x && go1.x + go1.width <= go2.x + go2.width && go1.y + go1.height >= go2.y && go1.y + go1.height <= go2.y + go2.height) {
			return true;
		}
		return false;
	}
	
	public static float correctOverlap(float position, float min, float max) {
		if (position <= min) {
			return min;
		} else if (position >= max){
			return max;
		} else {
			return position;
		}
	}
}
