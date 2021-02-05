package dev.eeasee.hud_hanger.render.renderer;

public abstract class ContainerHungRenderer extends HungGUIBaseRenderer {

    protected float containerWidthRatio = 176f / 256f;
    protected float containerHeightRatio = 166f / 256f;

    public ContainerHungRenderer(int id) {
        super(id);
    }
}
