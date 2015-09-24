package com.github.czyzby.autumn.mvc.component.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.autumn.annotation.method.Destroy;
import com.github.czyzby.autumn.annotation.method.Initiate;
import com.github.czyzby.autumn.annotation.stereotype.Component;
import com.github.czyzby.autumn.context.processor.method.MessageProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.dto.SkinData;
import com.github.czyzby.autumn.mvc.component.ui.processor.SkinAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.SkinAssetAnnotationProcessor;
import com.github.czyzby.autumn.mvc.config.AutumnActionPriority;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.kiwi.util.gdx.file.CommonFileExtension;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

/** Manages application's {@link com.badlogic.gdx.scenes.scene2d.ui.Skin}.
 *
 * @author MJ */
@Component
public class SkinService {
    private final Skin skin = new Skin();

    @Initiate(priority = AutumnActionPriority.TOP_PRIORITY)
    private void initiateSkin(final SkinAssetAnnotationProcessor skinAssetProcessor,
            final SkinAnnotationProcessor skinProcessor, final AssetService assetService,
            final MessageProcessor messageProcessor) {
        final SkinData skinData = skinProcessor.getSkinData();
        final String atlasPath = skinData.getPath() + CommonFileExtension.ATLAS;
        assetService.load(atlasPath, TextureAtlas.class);
        final TextureAtlas skinAtlas = assetService.finishLoading(atlasPath, TextureAtlas.class);

        final String[] fontPaths = skinData.getFonts();
        loadFonts(atlasPath, fontPaths, assetService);
        skin.addRegions(skinAtlas);
        assignFonts(skinData, fontPaths, assetService);

        skin.load(Gdx.files.internal(skinData.getPath() + CommonFileExtension.JSON));
        assignSkinAssetFields(skinAssetProcessor);
        messageProcessor.postMessage(AutumnMessage.SKIN_LOADED);
    }

    private static void loadFonts(final String atlasPath, final String[] fontPaths, final AssetService assetService) {
        if (fontPaths.length != 0) {
            final BitmapFontParameter loadingParameters = new BitmapFontParameter();
            loadingParameters.atlasName = atlasPath;
            for (final String fontPath : fontPaths) {
                assetService.finishLoading(fontPath, BitmapFont.class, loadingParameters);
            }
        }
    }

    private void assignFonts(final SkinData skinData, final String[] fontPaths, final AssetService assetService) {
        if (fontPaths.length != 0) {
            final String[] fontNames = skinData.getFontsNames();
            for (int fontIndex = 0; fontIndex < fontPaths.length; fontIndex++) {
                skin.add(fontNames[fontIndex], assetService.get(fontPaths[fontIndex], BitmapFont.class),
                        BitmapFont.class);
            }
        }
    }

    // Not added to SkinAssetAnnotationProcessor as it is a meta-component and cannot process posted events.
    // Public method felt somehow cheap (heh), while putting all skin-related logic here seemed like a good
    // idea at the time.
    @SuppressWarnings("unchecked")
    private void assignSkinAssetFields(final SkinAssetAnnotationProcessor skinAssetProcessor) {
        for (final Entry<String, Array<Pair<Field, Object>>> fieldsWithAssetName : skinAssetProcessor
                .getFieldsToInject()) {
            for (final Pair<Field, Object> fieldWithOwner : fieldsWithAssetName.value) {
                final Field field = fieldWithOwner.getFirst();
                final Object owner = fieldWithOwner.getSecond();
                try {
                    Reflection.setFieldValue(field, owner, skin.get(fieldsWithAssetName.key, field.getType()));
                } catch (final Exception exception) {
                    throw new AutumnRuntimeException(
                            "Unable to inject SkinAsset in field: " + field + " to component: " + owner, exception);
                }
            }
        }
        skinAssetProcessor.clearFieldsToInject();
    }

    /** @return application's main {@link com.badlogic.gdx.scenes.scene2d.ui.Skin} used to build views. */
    public Skin getSkin() {
        return skin;
    }

    @Destroy(priority = AutumnActionPriority.VERY_LOW_PRIORITY)
    private void dispose() {
        skin.dispose();
    }
}