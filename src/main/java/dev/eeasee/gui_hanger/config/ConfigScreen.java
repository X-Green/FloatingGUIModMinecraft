package dev.eeasee.gui_hanger.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends Screen {
    private ButtonListWidget list;

    private final Screen parent;

    ConfigScreen(Screen parent) {
        super(new LiteralText("GUIHanger Config Menu"));
        this.parent = parent;
    }

    private void updateState() {

    }

    @Override
    public void init() {
        this.list = new ButtonListWidget(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        this.list.addSingleOptionEntry(new BooleanOption(
                "sendScreenToServerIfSupported",
                gameOptions -> Configs.sendScreenToServerIfSupported,
                (gameOptions, aBoolean) -> Configs.sendScreenToServerIfSupported = aBoolean
        ));
        this.list.addSingleOptionEntry(new BooleanOption(
                "displayInClientWorld",
                gameOptions -> Configs.displayInClientWorld,
                (gameOptions, aBoolean) -> Configs.displayInClientWorld = aBoolean
        ));
        this.list.addSingleOptionEntry(new DoubleOption(
                "hangedGUIScale",
                0.05,
                2.05,
                0.05f,
                gameOptions -> (double) Configs.hangedGUIScale,
                (gameOptions, aDouble) -> Configs.onScaleChanged(aDouble.floatValue()),
                (gameOptions, doubleOption) -> String.valueOf(Configs.hangedGUIScale)
        ));

        this.children.add(this.list);
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, I18n.translate("gui.done"), (button) -> this.onClose()));
    }

    @Override
    public void onClose() {
        Configs.write();
        if (this.minecraft != null) {
            this.minecraft.openScreen(this.parent);
        }
    }

    @Override
    public void removed() {
        Configs.write();
        super.removed();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // return this.list.mouseReleased(mouseX, mouseY, button);
        return true;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.list.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, delta);
    }

}
