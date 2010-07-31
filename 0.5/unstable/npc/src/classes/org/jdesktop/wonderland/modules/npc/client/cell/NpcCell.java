/**
 * Open Wonderland
 *
 * Copyright (c) 2010, Open Wonderland Foundation, All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * The Open Wonderland Foundation designates this particular file as
 * subject to the "Classpath" exception as provided by the Open Wonderland
 * Foundation in the License file that accompanied this code.
 */

/**
 * Project Wonderland
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * Sun designates this particular file as subject to the "Classpath"
 * exception as provided by Sun in the License file that accompanied
 * this code.
 */
package org.jdesktop.wonderland.modules.npc.client.cell;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.GeometricUpdateListener;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import imi.character.avatar.Avatar;
import imi.character.avatar.AvatarContext.TriggerNames;
import imi.character.behavior.CharacterBehaviorManager;
import imi.character.behavior.GoTo;
import imi.character.statemachine.GameContext;
import imi.character.statemachine.GameContextListener;
import imi.character.statemachine.GameState;
import imi.character.statemachine.corestates.CycleActionState;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.mtgame.Entity;
import org.jdesktop.mtgame.RenderComponent;
import org.jdesktop.mtgame.RenderManager;
import org.jdesktop.mtgame.processor.WorkProcessor.WorkCommit;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.CellCache;
import org.jdesktop.wonderland.client.cell.CellRenderer;
import org.jdesktop.wonderland.client.cell.CellStatusChangeListener;
import org.jdesktop.wonderland.client.cell.ChannelComponent;
import org.jdesktop.wonderland.client.cell.annotation.UsesCellComponent;
import org.jdesktop.wonderland.client.cell.asset.AssetUtils;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuActionListener;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItemEvent;
import org.jdesktop.wonderland.client.contextmenu.SimpleContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.cell.ContextMenuComponent;
import org.jdesktop.wonderland.client.contextmenu.spi.ContextMenuFactorySPI;
import org.jdesktop.wonderland.client.jme.AvatarRenderManager.RendererUnavailable;
import org.jdesktop.wonderland.client.jme.ClientContextJME;
import org.jdesktop.wonderland.client.jme.SceneWorker;
import org.jdesktop.wonderland.client.jme.cellrenderer.AvatarJME;
import org.jdesktop.wonderland.client.login.ServerSessionManager;
import org.jdesktop.wonderland.client.scenemanager.event.ContextEvent;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.CellTransform;
import org.jdesktop.wonderland.modules.avatarbase.client.imi.ImiAvatarLoaderFactory;
import org.jdesktop.wonderland.modules.avatarbase.client.jme.cellrenderer.AvatarImiJME;
import org.jdesktop.wonderland.modules.avatarbase.client.jme.cellrenderer.AvatarImiJME.AvatarChangedListener;
import org.jdesktop.wonderland.modules.avatarbase.client.jme.cellrenderer.NameTagNode;
import org.jdesktop.wonderland.modules.avatarbase.client.jme.cellrenderer.PickGeometry;
import org.jdesktop.wonderland.modules.avatarbase.client.jme.cellrenderer.PickGeometry.PickBox;
import org.jdesktop.wonderland.modules.avatarbase.client.jme.cellrenderer.WlAvatarCharacter;
import org.jdesktop.wonderland.modules.avatarbase.common.cell.AvatarConfigInfo;
import org.jdesktop.wonderland.modules.avatarbase.common.cell.messages.AvatarConfigMessage;
import org.jdesktop.wonderland.modules.scriptingComponent.client.ScriptingActionClass;
import org.jdesktop.wonderland.modules.scriptingComponent.client.ScriptingComponent;
import org.jdesktop.wonderland.modules.scriptingComponent.client.ScriptingRunnable;

/**
 *
 * @author paulby
 * @author david <dmaroto@it.uc3m.es> UC3M - "Project España Virtual"
 */
public class NpcCell extends Cell
        implements CellStatusChangeListener, AvatarChangedListener,
                   ContextMenuActionListener
{
    private AvatarImiJME renderer;

    @UsesCellComponent
    private MovableNpcComponent movableNpc;

    @UsesCellComponent
    private ContextMenuComponent contextMenu;

    @UsesCellComponent
    private ScriptingComponent scriptingComponent;

    private final ContextMenuFactorySPI menuFactory;

    private String[] nameList = null;

    public NpcCell(CellID cellID, CellCache cellCache) {
        super(cellID, cellCache);

        menuFactory = new ContextMenuFactorySPI() {
            public ContextMenuItem[] getContextMenuItems(ContextEvent event) {
                return new ContextMenuItem[] {
                    new SimpleContextMenuItem("Controls...", NpcCell.this),
                    //new SimpleContextMenuItem("Pick Geometry...", NpcCell.this)
                };
            }
        };
    }

    public void actionPerformed(ContextMenuItemEvent event) {
        if (event.getContextMenuItem().getLabel().equals("Controls...")) {
            NpcControllerFrame frame = new NpcControllerFrame(getControls());
            frame.pack();
            frame.setVisible(true);
        } else if (event.getContextMenuItem().getLabel().equals("Pick Geometry...")) {
            PickGeometry pg = renderer.getPickGeometry();
            for (Spatial s : pg.getChildren()) {
                PickGeometryEditor editor = new PickGeometryEditor(pg, (PickBox) s, renderer.getAvatarCharacter());
                editor.pack();
                editor.setVisible(true);
            }
        }
    }

    @Override
    protected void setStatus(CellStatus status, boolean increasing) {
        super.setStatus(status, increasing);
        
        // If the Cell is being made active and increasing, then add the menu
        // item. Also add the proximity listener
        if (status == CellStatus.ACTIVE && increasing == true) {
            contextMenu.addContextMenuFactory(menuFactory);
            addStatusChangeListener(this);

            ScriptingActionClass sac = new ScriptingActionClass();
            sac.setName("NPC");
            sac.insertCmdMap("testit", testitRun);
            sac.insertCmdMap("move", moveRun);
            sac.insertCmdMap("selectAvatar", avatarSelectAvatarRun);
            sac.insertCmdMap("startForward", avatarStartForwardRun);
            sac.insertCmdMap("stopForward", avatarStopForwardRun);
            sac.insertCmdMap("startBack", avatarStartBackRun);
            sac.insertCmdMap("stopBack", avatarStopBackRun);
            sac.insertCmdMap("startLeft", avatarStartLeftRun);
            sac.insertCmdMap("stopLeft", avatarStopLeftRun);
            sac.insertCmdMap("startRight", avatarStartRightRun);
            sac.insertCmdMap("stopRight", avatarStopRightRun);
            sac.insertCmdMap("dumpAnimations", avatarDumpAnimationsRun);
            sac.insertCmdMap("runAnimation", avatarRunAnimationRun);
            sac.insertCmdMap("stopAnimation", avatarStopAnimationRun);
            sac.insertCmdMap("attachNpcName", avatarAttachNpcNameRun);
 //           System.out.println("in sac stuff - nameList = " + nameList);
            scriptingComponent.putActionObject(sac);

            return;
        }

        // if the Cell is being brought back down through the ACTIVE state,
        // then remove the menu item
        if (status == CellStatus.ACTIVE && increasing == false) {
            contextMenu.removeContextMenuFactory(menuFactory);
            removeStatusChangeListener(this);
            return;
        }
    }

        public void testit(float x, float y, float z)
        {
        System.out.println("testit x = " + x + " y = " + y + " z = " + z);
        }

    ScriptingRunnable testitRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            testit(x, y, z);
            System.out.println("ScriptingActionClass - enter testit");
            }
        };


    ScriptingRunnable moveRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            System.out.println("ScriptingActionClass - enter moveRun");
            move(x, y, z);
            }
        };


    public void avatarSelectAvatar(String avatar)
        {
        ChannelComponent cc = NpcCell.this.getComponent(ChannelComponent.class);

        // From the partial URI, add the module prefix
        String uri = "wla://avatarbaseart/" + avatar;
        String urlString = null;
        try {
            urlString = AssetUtils.getAssetURL(uri, NpcCell.this).toExternalForm();
        } catch (java.net.MalformedURLException excp) {
            logger.log(Level.WARNING, "Unable to form URL from " + uri, excp);
            return;
        }

        // Form up a message and send
        String className = ImiAvatarLoaderFactory.class.getName();
        AvatarConfigInfo info = new AvatarConfigInfo(urlString, className);
        cc.send(AvatarConfigMessage.newRequestMessage(info));
        }

    ScriptingRunnable avatarSelectAvatarRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarSelectAvatar(avatar);
            System.out.println("ScriptingActionClass - enter avatarStartForward");
            }
        };

    public void avatarStartForward()
        {
        renderer.getAvatarCharacter().triggerActionStart(TriggerNames.Move_Forward);
        }

    ScriptingRunnable avatarStartForwardRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStartForward();
            System.out.println("ScriptingActionClass - enter avatarStartForward");
            }
        };

    public void avatarRunAnimation(String animation)
        {
        renderer.getAvatarCharacter().playAnimation(animation);
        }

    ScriptingRunnable avatarRunAnimationRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarRunAnimation(animation);
            System.out.println("ScriptingActionClass - enter avatarRunAnimation");
            }
        };

    public void avatarStopAnimation()
        {
        renderer.getAvatarCharacter().stop();
        }

    ScriptingRunnable avatarStopAnimationRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStopAnimation();
            System.out.println("ScriptingActionClass - enter avatarStopAnimation");
            }
        };

    public String[] avatarDumpAnimations()
        {
        int     i = 0;
        nameList = new String[50];

        for(String anim : renderer.getAvatarCharacter().getAnimationNames())
            {
                System.out.println("Avatar animation = " + anim);
                nameList[i] = anim;
                i++;
            }
        System.out.println("in avatarDumpAnimations - list = " + nameList);
        return nameList;
        }

    ScriptingRunnable avatarDumpAnimationsRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            String[] list = avatarDumpAnimations();

            setNameArray(list);
            System.out.println("In npc avatarDumpAnimationsRun - exit avatarDumpAnimations - list = " + list + " first = " + list[0]);
            }
        };

    public void avatarStopForward()
        {
        renderer.getAvatarCharacter().triggerActionStop(TriggerNames.Move_Forward);
        }

    ScriptingRunnable avatarStopForwardRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStopForward();
            System.out.println("ScriptingActionClass - enter avatarStopForward");
            }
        };

    public void avatarStartBack()
        {
        renderer.getAvatarCharacter().triggerActionStart(TriggerNames.Move_Back);
        }

    ScriptingRunnable avatarStartBackRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStartBack();
            System.out.println("ScriptingActionClass - enter avatarStartBack");
            }
        };

    public void avatarStopBack()
        {
        renderer.getAvatarCharacter().triggerActionStop(TriggerNames.Move_Back);
        }

    ScriptingRunnable avatarStopBackRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStopBack();
            System.out.println("ScriptingActionClass - enter avatarStopBack");
            }
        };

    public void avatarStartLeft()
        {
        renderer.getAvatarCharacter().triggerActionStart(TriggerNames.Move_Left);
        }

    ScriptingRunnable avatarStartLeftRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStartLeft();
            System.out.println("ScriptingActionClass - enter avatarStartLeft");
            }
        };

    public void avatarStopLeft()
        {
        renderer.getAvatarCharacter().triggerActionStop(TriggerNames.Move_Left);
        }

    ScriptingRunnable avatarStopLeftRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStopLeft();
            System.out.println("ScriptingActionClass - enter avatarStopLeft");
            }
        };

    public void avatarStartRight()
        {
        renderer.getAvatarCharacter().triggerActionStart(TriggerNames.Move_Right);
        }

    ScriptingRunnable avatarStartRightRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStartRight();
            System.out.println("ScriptingActionClass - enter avatarStartRight");
            }
        };

    public void avatarStopRight()
        {
        renderer.getAvatarCharacter().triggerActionStop(TriggerNames.Move_Right);
        }

    ScriptingRunnable avatarStopRightRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            avatarStopRight();
            System.out.println("ScriptingActionClass - enter avatarStopRight");
            }
        };

    ScriptingRunnable avatarAttachNpcNameRun = new ScriptingRunnable()
        {
        @Override
        public void run()
            {
            attachNpcName(avatar);
            System.out.println("ScriptingActionClass - enter avatarStopRight");
            }
        };

    public void cellStatusChanged(Cell cell, CellStatus status) {
        if (status == CellStatus.ACTIVE) {
            // do this in a status change listener to ensure that the renderer
            // is created at the time we want to add a listener
            renderer.addAvatarChangedListener(this);
            if (renderer.getAvatarCharacter() != null) {
                avatarChanged(renderer.getAvatarCharacter());
            }
        }
    }

    public void avatarChanged(Avatar avatar) {
        SceneWorker.addWorker(new WorkCommit() {
            public void commit() {
                attachEditorGeometry();
                attachCellLocationUpdater();
                attachAnimationListener();
                attachNpcName("ralph");
            }
        });
    }

    @Override
    protected CellRenderer createCellRenderer(RendererType rendererType) {
        CellRenderer ret = null;
        switch (rendererType) {
            case RENDERER_2D:
                // No 2D Renderer yet
                break;
            case RENDERER_JME:
                try {
                    ServerSessionManager session = getCellCache().getSession().getSessionManager();
                    ret = ClientContextJME.getAvatarRenderManager().createRenderer(session, this);

                    if (ret instanceof AvatarImiJME) {
                        renderer = (AvatarImiJME) ret;
                    }
                } catch (RendererUnavailable ex) {
                    Logger.getLogger(NpcCell.class.getName()).log(Level.SEVERE, null, ex);
                    ret = new AvatarJME(this);
                }
                break;
        }

        return ret;
    }

    public NpcControls getControls() {
        return new NpcControls() {
            public void triggerActionStart(TriggerNames trigger) {
                renderer.getAvatarCharacter().triggerActionStart(trigger);
            }

            public void triggerActionStop(TriggerNames trigger) {
                renderer.getAvatarCharacter().triggerActionStop(trigger);
            }

            public Iterable<String> getAnimations() {
                return renderer.getAvatarCharacter().getAnimationNames();
            }

            public void playAnimation(String animation) {
                renderer.getAvatarCharacter().playAnimation(animation);
            }

            public void goTo(float x, float y, float z) {
                move(x, y, z);
            }
        };
    }

    protected void move(float x, float y, float z) {
        GameContext context = renderer.getAvatarCharacter().getContext();
        CharacterBehaviorManager helm = context.getBehaviorManager();
        helm.clearTasks();
        helm.setEnable(true);
        helm.addTaskToTop(new GoTo(new Vector3f(x, y, z), context));
    }

    private void attachEditorGeometry() {
        Entity e = renderer.getEntity();
        if (e.getComponent(RenderComponent.class) == null) {
            Box b = new Box("Avatar editor", Vector3f.ZERO, 0.4f, 0.95f, 0.1f);
            b.setLocalTranslation(new Vector3f(0f, 1f, 0f));
            b.setModelBound(new BoundingBox(Vector3f.ZERO, b.getXExtent(),
                                            b.getYExtent(), b.getZExtent()));
            b.updateGeometricState(0, true);
            b.setCullHint(Spatial.CullHint.Always);

            Node n = new Node("Avatar editor");
            n.attachChild(b);

            RenderManager rm =  ClientContextJME.getWorldManager().getRenderManager();
            RenderComponent rc = rm.createRenderComponent(n);
            rc.setAttachPoint(renderer.getAvatarCharacter().getJScene().getExternalKidsRoot());

            e.addComponent(RenderComponent.class, rc);
            ClientContextJME.getWorldManager().addToUpdateList(n);
        }
    }

    private void attachNpcName(String name)
        {
        renderer.getAvatarCharacter();
        Node extKids = renderer.getAvatarCharacter().getJScene().getExternalKidsRoot();
        extKids.attachChild(new NameTagNode(name, 2, false, false, false));

        }

    private void attachCellLocationUpdater() {
        Node extKids = renderer.getAvatarCharacter().getJScene().getExternalKidsRoot();
        extKids.addGeometricUpdateListener(new GeometricUpdateListener() {
            public void geometricDataChanged(Spatial sptl) {
                CellTransform xform = new CellTransform(sptl.getWorldRotation(),
                                                        sptl.getWorldTranslation(),
                                                        sptl.getWorldScale().x);

                movableNpc.geometryChanged(xform);
            }
        });
    }

    private void attachAnimationListener()
        {
        final WlAvatarCharacter character = renderer.getAvatarCharacter();
        character.getContext().addGameContextListener(new GameContextListener()
            {
            public void trigger(boolean pressed, int trigger, Vector3f translation, Quaternion rotation)
                {
                GameState state = character.getContext().getCurrentState();
                String animationName=null;
                if (state instanceof CycleActionState)
                    {
                    animationName = character.getContext().getState(CycleActionState.class).getAnimationName();
                    }

                movableNpc.localMoveRequest(new CellTransform(rotation, translation), trigger, pressed, animationName, null);
                }
            });
        }
}
