
import java.awt.event.KeyListener;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;


public abstract class AnimListener extends JFrame implements GLEventListener, KeyListener {
 
    protected String assetsFolderName = "Assets";
    
    void setGLCanvas(GLCanvas glcanvas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
