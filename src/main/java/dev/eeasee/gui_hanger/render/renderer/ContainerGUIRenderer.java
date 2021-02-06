package dev.eeasee.gui_hanger.render.renderer;

public abstract class ContainerGUIRenderer extends GUIRendererBase {

    protected float containerWidthRatio = 176f / 256f;
    protected float containerHeightRatio = 166f / 256f;

    public ContainerGUIRenderer(int id) {
        super(id);
    }

}
