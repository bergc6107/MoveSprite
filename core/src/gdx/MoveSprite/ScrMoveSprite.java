package gdx.MoveSprite;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

//public class ScrMoveSprite implements ApplicationListener {
public class ScrMoveSprite implements Screen {

    Game game;
    private Texture txDot;
    private Texture txSquare;
    private Texture txEnemy;
    private Sound dropSound;
    private Music rainMusic;
    private SpriteBatch batch;
    private Sprite sprDot, sprSquare, sprEnemy; // a Sprite allows you to get the bounding rectangle
    private OrthographicCamera camera;
    private Array<Sprite> arsprDrop; // use an array of Sprites rather than rectangles
    private Array<Sprite> arsprEnemy;
    private long lastDropTime;
    private long lastEnemyTime;
    private int nScore;
    private int nLives;
    private BitmapFont font;
    private int spawnMillis;

    public ScrMoveSprite(Game _game) {
        game = _game;
        // load the images for the droplet and the bucket, 64x64 pixels each
        txDot = new Texture(Gdx.files.internal("dot.png"));
        txSquare = new Texture(Gdx.files.internal("square.png"));
        txEnemy = new Texture(Gdx.files.internal("enemy.png"));
        font = new BitmapFont();
        nLives = 3;
        spawnMillis = 1000;
        sprSquare = new Sprite(txSquare);
        sprDot = new Sprite(txDot);
        sprEnemy = new Sprite(txEnemy);

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        arsprDrop = new Array<Sprite>();// array of Sprites rather than Rectangles
        batch = new SpriteBatch();
        arsprEnemy = new Array<Sprite>();
        spawnRaindrop();
        spawnEnemy();
    }

    private void spawnRaindrop() {
        Sprite sprDrop = new Sprite(txDot);
        sprDrop.setX(MathUtils.random(0, 800 - 64));
        sprDrop.setY(480);
        arsprDrop.add(sprDrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void spawnEnemy() {
        Sprite sprEnemy = new Sprite(txEnemy);
        sprEnemy.setX(MathUtils.random(0, 800 - 64));
        sprEnemy.setY(480);
        arsprEnemy.add(sprEnemy);
        lastEnemyTime = TimeUtils.millis();
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        txDot.dispose();
        txSquare.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // notice that when you implement a Screen, "render" requires  the float delta to be passed.
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        batch.begin();
        if (nLives > 0) {
            batch.draw(sprSquare, sprSquare.getX(), sprSquare.getY());
            batch.draw(sprEnemy, sprEnemy.getX(), sprEnemy.getY());

            for (Sprite sprDrop : arsprDrop) {
                batch.draw(sprDrop, sprDrop.getX(), sprDrop.getY());
            }
        }
        font.draw(batch, Integer.toString(nScore), 10, 10);
        font.draw(batch, Integer.toString(nLives), 200, 10);
        font.draw(batch, Integer.toString(spawnMillis), 400, 10);
        batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            sprSquare.setX(touchPos.x - 64 / 2);
        }
        /*if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
         if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();*/
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            sprSquare.setX(sprSquare.getX() - 500 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Keys.A)) {
            sprEnemy.setX(sprEnemy.getX() - 200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            sprSquare.setY(sprSquare.getY() + 500 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            sprEnemy.setY(sprEnemy.getY() + 200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            sprSquare.setY(sprSquare.getY() - 500 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            sprEnemy.setY(sprEnemy.getY() - 200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            sprSquare.setX(sprSquare.getX() + 500 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            sprEnemy.setX(sprEnemy.getX() + 200 * Gdx.graphics.getDeltaTime());
        }

        // make sure the bucket stays within the screen bounds
        /*if(bucket.x < 0) bucket.x = 0;
         if(bucket.x > 800 - 64) bucket.x = 800 - 64;*/
        if (sprSquare.getX() < 0) {
            sprSquare.setX(0);
        }
        if (sprEnemy.getX() < 0) {
            sprEnemy.setX(0);
        }
        if (sprSquare.getX() > 800 - 64) {
            sprSquare.setX(800 - 64);
        }
        if (sprEnemy.getX() > 800 - 64) {
            sprEnemy.setX(800 - 64);
        }

        // check if we need to create a new raindrop
        spawnMillis = 1000 - (nScore * 5 / 2);
        if (TimeUtils.nanoTime() - lastDropTime > 1000000 * spawnMillis) {
            spawnRaindrop();
        }
        if (TimeUtils.nanoTime() - lastEnemyTime > 1000000 * spawnMillis) {
            spawnEnemy();
        }

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we play back
        // a sound effect as well.
        //Iterator<Rectangle> iter = raindrops.iterator();
        Iterator<Sprite> iter = arsprDrop.iterator();
        while (iter.hasNext()) {
            Sprite sprDot = iter.next();
            // lower the drop
            //raindrop.y -= (150 + 2*nScore) * Gdx.graphics.getDeltaTime();
            sprDot.setY(sprDot.getY() - (150 + 2 * nScore) * Gdx.graphics.getDeltaTime());
            if (sprDot.getY() + 64 < 0) {
                nLives--;
                iter.remove();
            }
            if (sprDot.getBoundingRectangle().overlaps(sprSquare.getBoundingRectangle())) {
                dropSound.play();
                nScore++;
                iter.remove();
            }
            if (sprDot.getBoundingRectangle().overlaps(sprEnemy.getBoundingRectangle())) {
                dropSound.play();
                nScore--;
                iter.remove();
            }
        }
    }

    @Override
    public void hide() {
    }
}
