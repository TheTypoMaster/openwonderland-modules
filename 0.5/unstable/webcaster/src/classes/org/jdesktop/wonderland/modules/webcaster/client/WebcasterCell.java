package org.jdesktop.wonderland.modules.webcaster.client;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jdesktop.wonderland.common.cell.state.CellClientState;
import org.jdesktop.wonderland.modules.webcaster.client.jme.cellrenderer.WebcasterCellRenderer;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.Cell.RendererType;
import org.jdesktop.wonderland.client.cell.CellCache;
import org.jdesktop.wonderland.client.cell.CellRenderer;
import org.jdesktop.wonderland.client.cell.annotation.UsesCellComponent;
import org.jdesktop.wonderland.client.cell.asset.AssetUtils;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuActionListener;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItemEvent;
import org.jdesktop.wonderland.client.contextmenu.SimpleContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.cell.ContextMenuComponent;
import org.jdesktop.wonderland.client.contextmenu.spi.ContextMenuFactorySPI;
import org.jdesktop.wonderland.client.hud.CompassLayout.Layout;
import org.jdesktop.wonderland.client.hud.HUD;
import org.jdesktop.wonderland.client.hud.HUDComponent;
import org.jdesktop.wonderland.client.hud.HUDManagerFactory;
import org.jdesktop.wonderland.client.scenemanager.event.ContextEvent;
import org.jdesktop.wonderland.client.utils.AudioResource;
import org.jdesktop.wonderland.client.utils.VideoLibraryLoader;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.modules.webcaster.client.utils.RTMPOut;

public class WebcasterCell extends Cell
{
    private static final boolean VIDEO_AVAILABLE = VideoLibraryLoader.loadVideoLibraries();
    private static final String SERVER_URL;

    static
    {
        SERVER_URL = ("rtmp://" + System.getProperty("wonderland.server.url").substring(System.getProperty("wonderland.server.url").indexOf(":") + 3)).substring(0, System.getProperty("wonderland.server.url").lastIndexOf(":")) + ":1935/";
    }

    private WebcasterCellRenderer renderer = null;

    @UsesCellComponent private ContextMenuComponent contextComp = null;
    private ContextMenuFactorySPI menuFactory = null;

    private HUD mainHUD;
    private HUDComponent hudComponent;
    private ControlPanel controlPanel;

    private boolean isRecording = false;
    private RTMPOut streamOutput;

    private AudioResource startSound = null;

    public WebcasterCell(CellID cellID, CellCache cellCache) {
        super(cellID, cellCache);
        try{startSound = new AudioResource(AssetUtils.getAssetURL("wla://webcaster/startsound.au"));}catch(MalformedURLException e){}
    }

    public void showControlPanel(){
        try
        {
            SwingUtilities.invokeLater(new Runnable () {
                public void run () {
                    hudComponent.setVisible(true);
                }
            });
        } catch (Exception x) {
            throw new RuntimeException("Cannot add hud component to main hud");
        }
    }

    public JComponent getCaptureComponent(){
        return renderer.getCaptureComponent();
    }

    public void setRecording(boolean isRecording){
        this.isRecording = isRecording;
        renderer.setButtonRecordingState(isRecording);
        
        if (!isRecording){
            try{
                streamOutput.close();
                streamOutput = null;
            }
            catch(Exception e){}
        }

        startSound.play();
    }

    public boolean getRecording(){
        return isRecording;
    }

    public void write(BufferedImage frame)
    {
        if (streamOutput == null){
            streamOutput = new RTMPOut(SERVER_URL + "live/" + controlPanel.getStreamName());
        }
        
        streamOutput.write(frame);
    }
    
    @Override
    public void setStatus(CellStatus status, boolean increasing)
    {
        super.setStatus(status, increasing);

        switch (status)
        {
            case RENDERING:
                if (increasing)
                {
                    if (mainHUD == null){
                        mainHUD = HUDManagerFactory.getHUDManager().getHUD("main");

                        try
                        {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    controlPanel = new ControlPanel(WebcasterCell.this);
                                    hudComponent = mainHUD.createComponent(controlPanel);
                                    hudComponent.setPreferredLocation(Layout.SOUTHWEST);
                                    mainHUD.addComponent(hudComponent);
                                }
                            });
                        }
                        catch (Exception x) {
                            throw new RuntimeException("Cannot create construct panel");
                        }
                    }

                    if (menuFactory == null)
                    {
                        menuFactory = new ContextMenuFactorySPI()
                        {
                            public ContextMenuItem[] getContextMenuItems(ContextEvent event) {
                                return new ContextMenuItem[]{new SimpleContextMenuItem("Control Panel", new ContextMenuActionListener(){
                                    public void actionPerformed(ContextMenuItemEvent event){
                                        try
                                        {
                                            SwingUtilities.invokeLater(new Runnable () {
                                                public void run () {
                                                    hudComponent.setVisible(true);
                                                }
                                            });
                                        } catch (Exception x) {
                                            throw new RuntimeException("Cannot add hud component to main hud");
                                        }
                                    }
                                })};
                            }
                        };
                        contextComp.addContextMenuFactory(menuFactory);
                    }
                }

                break;
            case DISK:
                if (!increasing){
                    setRecording(false);
                }
                break;
        }
    }
    
    @Override
    public void setClientState(CellClientState state){
        super.setClientState(state);
    }

    @Override
    protected CellRenderer createCellRenderer(RendererType rendererType)
    {
        if (rendererType == RendererType.RENDERER_JME)
        {
            this.renderer = new WebcasterCellRenderer(this);
            return this.renderer;
        }
        else
        {
            return super.createCellRenderer(rendererType);
        }
    }
}