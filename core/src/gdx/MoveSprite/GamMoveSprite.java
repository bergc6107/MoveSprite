/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.MoveSprite;
import com.badlogic.gdx.Game;

/**
 *
 * @author G
 */
public class GamMoveSprite extends Game{
    @Override
	public void create () {
        this.setScreen(new ScrMoveSprite(this));
	}

	@Override
	public void render () {
        super.render();
	}
    
    
}
