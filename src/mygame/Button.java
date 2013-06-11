/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.ui.Picture;

/**
 *
 * @author normenhansen
 */
public class Button extends Picture
{
    public float width, height;
    public String fileName, hoverFileName;
    public AssetManager assetManager;
    public boolean isFocused = false;
    
    public Button(AssetManager assetManager, String name, String fileName)
    {
        super(name);
        this.assetManager = assetManager;
        this.fileName = fileName;
        setImage(assetManager, "Menu/" + fileName, true);
    }
    
    @Override
    public void setWidth(float width)
    {
        super.setWidth(width);
        this.width = width;
    }
    
    @Override
    public void setHeight(float height)
    {
        super.setHeight(height);
        this.height = height;
    }
    
    public float getX()
    {
        return getLocalTranslation().x;
    }
    
    public float getY()
    {
        return getLocalTranslation().y;
    }
    
    public void setHoverImage(String filename)
    {
        hoverFileName = filename;
    }
    
    public void onMouseEnter(InputManager inputManager)
    {
        float cursorX = inputManager.getCursorPosition().getX();
        float cursorY = inputManager.getCursorPosition().getY();
        
        if(cursorX > getX() && cursorX < getX() + width && cursorY > getY() && cursorY < getY() + height)
        {
            setImage(assetManager, "Menu/" + hoverFileName, true);
            isFocused = true;
        }
        else
            isFocused = false;
    }
    
    public void onMouseLeave()
    {
        if(!isFocused)
            setImage(assetManager, "Menu/" + fileName, true);
    }
}
