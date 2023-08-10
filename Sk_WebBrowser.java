
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

/**
 * A basic multi-window web browser.  This class is responsible for
 * creating new windows and for maintaining a list of currently open
 * windows.  The program ends when all windows have been closed.
 * The windows are of type Sk_BrowserWindow.  The program also requires
 * the class SimpleDialogs.  The first window, which opens when the
 * program starts, goes to "https://www.core2web.in/privacypolicy.html.
 */
public class Sk_WebBrowser extends Application {

    public static void main(String[] Sk_args) {
        launch(Sk_args);
    }
    //----------------------------------------------------------------------------------------------------
    
    private ArrayList<Sk_BrowserWindow> Sk_openWindows;  // list of currently open web browser windows
    private Rectangle2D Sk_screenRect;                // usable area of the primary screen
    private double Sk_locationX, Sk_locationY;           // location for next window to be opened
    private double Sk_windowWidth, Sk_windowHeight;      // window size, computed from Sk_screenRect
    private int Sk_untitledCount;                     // how many "Untitled" window titles have been used
    
    
    /* Opens a window that will load the Sk_URL https://www.core2web.in/privacypolicy.html
     * (the front page of the textbook in which this program is an example).
     * Note that the Stage parameter to this method is never used.
     */
    public void start(Stage stage) {
        
        Sk_openWindows = new ArrayList<Sk_BrowserWindow>();  // List of open windows.
        
        Sk_screenRect = Screen.getPrimary().getVisualBounds();
        
           // (Sk_locationX,Sk_locationY) will be the location of the upper left
           // corner of the next window to be opened.  For the first window,
           // the window is moved a little down and over from the top-left
           // corner of the primary screen's visible bounds.
        Sk_locationX = Sk_screenRect.getMinX() + 30;
        Sk_locationY = Sk_screenRect.getMinY() + 20;
        
           // The window size depends on the height and width of the screen's
           // visual bounds, allowing some extra space so that it will be
           // possible to stack several windows, each displaced from the
           // previous one.  (For aesthetic reasons, limit the width to be
           // at most 1.6 times the height.)
        Sk_windowHeight = Sk_screenRect.getHeight() - 160;
        Sk_windowWidth = Sk_screenRect.getWidth() - 130;
        if (Sk_windowWidth > Sk_windowHeight*1.6)
            Sk_windowWidth = Sk_windowHeight*1.6;
        
           // Open the first window, showing the front page of this textbook.
        Sk_newBrowserWindow("www.google.com");

    } // end start()
    
    /**
     * Get the list of currently open windows.  The browser windows use this
     * list to construct their Window menus.
     * A package-private method that is meant for use only in Sk_BrowserWindow.java.
     */
    ArrayList<Sk_BrowserWindow> getOpenWindowList() {
        return Sk_openWindows;
    }
    
    /**
     * Get the number of window titles of the form "Untitled XX" that have been
     * used.  A new window that is opened with a null Sk_URL gets a title of
     * that form.  This method is also used in Sk_BrowserWindow to provide a
     * title for any web page that does not itself provide a title for the page.
     * A package-private method that is meant for use only in Sk_BrowserWindow.java.
     */
    int Sk_getNextUntitledCount() {
        return ++Sk_untitledCount;
    }
    
    /**
     * Open a new browser window.  If Sk_url is non-null, the window will load that Sk_URL.
     * A package-private method that is meant for use only in Sk_BrowserWindow.java.
     * This method manages the locations for newly opened windows.  After a window
     * opens, the next window will be offset by 30 pixels horizontally and by 20
     * pixels vertically from the location of this window; but if that makes the
     * window extend outside Sk_screenRect, the horizontal or vertical position will
     * be reset to its minimal value.
     */
    void Sk_newBrowserWindow(String Sk_url) {
        Sk_BrowserWindow window = new Sk_BrowserWindow(this,Sk_url);
        Sk_openWindows.add(window);   // Add new window to open window list.
        window.setOnHidden( e -> {
                // Called when the window has closed.  Remove the window
                // from the list of open windows.
            Sk_openWindows.remove( window );
            System.out.println("Number of open windows is " + Sk_openWindows.size());
            if (Sk_openWindows.size() == 0) {
                // Program ends automatically when all windows have been closed.
                System.out.println("Program will end because all windows have been closed");
            }
        });
        if (Sk_url == null) {
            window.setTitle("Sk_Untitled " + Sk_getNextUntitledCount());
        }
        window.setX(Sk_locationX);         // set location and size of the window
        window.setY(Sk_locationY);
        window.setWidth(Sk_windowWidth);
        window.setHeight(Sk_windowHeight);
        window.show();
        Sk_locationX += 30;    // set up location of NEXT window
        Sk_locationY += 20;
        if (Sk_locationX + Sk_windowWidth + 10 > Sk_screenRect.getMaxX()) {
                // Window would extend past the right edge of the screen,
                // so reset Sk_locationX to its original value.
            Sk_locationX = Sk_screenRect.getMinX() + 30;
        }
        if (Sk_locationY + Sk_windowHeight + 10 > Sk_screenRect.getMaxY()) {
                // Window would extend past the bottom edge of the screen,
                // so reset Sk_locationY to its original value.
            Sk_locationY = Sk_screenRect.getMinY() + 20;
        }
    }
    
    
} // end WebBrowser