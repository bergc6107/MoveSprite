package gdx.MoveSprite.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdx.MoveSprite.GamMoveSprite;
import gdx.MoveSprite.ScrMoveSprite;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GamMoveSprite(), config);
	}
}
