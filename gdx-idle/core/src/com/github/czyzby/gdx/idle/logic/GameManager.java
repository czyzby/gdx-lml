package com.github.czyzby.gdx.idle.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StringBuilder;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.mvc.component.i18n.LocaleService;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.gdx.idle.logic.data.LocationData;
import com.github.czyzby.gdx.idle.logic.data.MonsterData;
import com.github.czyzby.gdx.idle.view.GameView;
import com.github.czyzby.gdx.idle.view.dialog.LossDialog;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;

// This is ugly. Focus on the framework, forget about the logic.
@Component
public class GameManager {

    private static final int MONSTERS_PER_LOCATION = 5;
    private static final int MAX_DAMAGE_MODIFICATOR = 9;
    private static final int MIN_DAMAGE_MODIFICATOR = 10;
    private static final int INITIAL_HEALTH = 75;
    private static final int CLICK_DAMAGE_MODIFICATOR = 3;
    public static final float ATTACK_DELAY = 2f;
    private static final int MIN_PLAYER_DAMAGE = 2, MAX_PLAYER_DAMAGE = 6;

    @Inject private GameView gameView;
    @Inject private DamageLabelPool damageLabelPool;
    @Inject private InterfaceService interfaceService;
    @Inject private LocaleService localeService;
    @Inject private MusicService musicService;

    private boolean regionsAssigned;
    @Asset("game/locations.atlas") private TextureAtlas locationsAtlas;
    @Asset("game/monsters.atlas") private TextureAtlas monstersAtlas;
    @Asset(value = { "sfx/sounds/punch0.ogg", "sfx/sounds/punch1.ogg", "sfx/sounds/punch2.ogg", "sfx/sounds/punch3.ogg",
            "sfx/sounds/punch4.ogg" }, type = Sound.class) private Array<Sound> hitSounds;
    @Asset(value = { "sfx/sounds/heal0.ogg", "sfx/sounds/heal1.ogg", "sfx/sounds/heal2.ogg" },
            type = Sound.class) private Array<Sound> healSounds;

    private Array<LocationData> locations;

    private LocationData currentLocation;
    private MonsterData currentMonster;

    private final ObjectSet<String> monstersKilled = GdxSets.newSet();
    private final StringBuilder labelBuilder = new StringBuilder();
    private int currentPlayerHealthAmount;
    private int currentMonsterHealthAmount;
    private int monstersKilledOnLocation;

    @OnMessage(AutumnMessage.ASSETS_LOADED)
    private void assignRegions() {
        if (!regionsAssigned) {
            regionsAssigned = true;
            interfaceService.getSkin().addRegions(locationsAtlas);
            interfaceService.getSkin().addRegions(monstersAtlas);
        }
    }

    @Initiate
    @SuppressWarnings("unchecked")
    private void loadGameData() {
        final Json json = new Json();
        json.addClassTag("location", LocationData.class);
        json.addClassTag("monster", MonsterData.class);
        locations = json.fromJson(Array.class, LocationData.class, Gdx.files.internal("game/locations.json"));
    }

    public void initiateGame() {
        monstersKilled.clear();
        currentPlayerHealthAmount = getTotalPlayerHealthAmount();
        setLabelText(0, gameView.monstersKilledLabel);
        changeLocation();
        updatePlayerHealth();
        updateMonsterHealth();
    }

    private void updatePlayerHealth() {
        gameView.playerHealthBar.setValue((float) currentPlayerHealthAmount / (float) getTotalPlayerHealthAmount());
        setLabelText(currentPlayerHealthAmount, gameView.playerHealthLabel);
    }

    private void setLabelText(final int value, final Label label) {
        Strings.clearBuilder(labelBuilder);
        labelBuilder.append(value);
        label.setText(labelBuilder);
    }

    private void updateMonsterHealth() {
        gameView.monsterHealthBar.setValue((float) currentMonsterHealthAmount / (float) currentMonster.health);
        setLabelText(currentMonsterHealthAmount, gameView.monsterHealthLabel);
    }

    private int getTotalPlayerHealthAmount() {
        return INITIAL_HEALTH + monstersKilled.size;
    }

    private void changeLocation() {
        currentLocation = locations.random();
        monstersKilledOnLocation = 0;
        gameView.locationImage.setDrawable(interfaceService.getSkin(), currentLocation.name);
        gameView.locationLabel.setText(localeService.getI18nBundle().format(currentLocation.name));
        changeMonster();
    }

    private void changeMonster() {
        currentMonster = currentLocation.getRandomMonster();
        gameView.monsterImage.setDrawable(interfaceService.getSkin(), currentMonster.name);
        gameView.monsterImage.addAction(Actions.sequence(Actions.alpha(0f), Actions.alpha(1f, 0.1f)));
        gameView.monsterLabel.setText(localeService.getI18nBundle().format(currentMonster.name));
        currentMonsterHealthAmount = currentMonster.health;
        updateMonsterHealth();
    }

    public void update() {
        dealPlayerDamage(getMonsterDamage(), false);
        dealMonsterDamage(getPlayerDamage());
    }

    private void dealMonsterDamage(final int damage) {
        if (currentPlayerHealthAmount <= 0) {
            return;
        }
        musicService.play(hitSounds.random());
        currentMonsterHealthAmount -= damage;
        damageLabelPool.attachLabel(damage, gameView.monsterImage, damage <= 0);
        if (currentMonsterHealthAmount <= 0) {
            killMonster();
        }
        updateMonsterHealth();
    }

    private void dealPlayerDamage(final int damage, final boolean heal) {
        if (currentPlayerHealthAmount <= 0 || currentPlayerHealthAmount == getTotalPlayerHealthAmount() && heal) {
            return;
        }
        if (heal) {
            musicService.play(healSounds.random());
        }
        currentPlayerHealthAmount -= damage;
        damageLabelPool.attachLabel(Math.abs(damage), gameView.playerImage, heal);
        currentPlayerHealthAmount = Math.min(getTotalPlayerHealthAmount(), Math.max(currentPlayerHealthAmount, 0));
        updatePlayerHealth();
        if (currentPlayerHealthAmount <= 0) {
            interfaceService.showDialog(LossDialog.class);
        }
    }

    private void killMonster() {
        monstersKilled.add(currentMonster.name);
        setLabelText(monstersKilled.size, gameView.monstersKilledLabel);
        if (++monstersKilledOnLocation == MONSTERS_PER_LOCATION) {
            changeLocation();
        } else {
            changeMonster();
        }
    }

    private int getMonsterDamage() {
        return MathUtils.random(currentMonster.damageMin, currentMonster.damageMax);
    }

    private int getPlayerDamage() {
        return MathUtils.random(MIN_PLAYER_DAMAGE + monstersKilled.size / MIN_DAMAGE_MODIFICATOR,
                MAX_PLAYER_DAMAGE + monstersKilled.size / MAX_DAMAGE_MODIFICATOR);
    }

    public void handlePlayerClick() {
        dealPlayerDamage(-getPlayerDamage() / CLICK_DAMAGE_MODIFICATOR, true);
    }

    public void handleMonsterClick() {
        dealMonsterDamage(getPlayerDamage() / CLICK_DAMAGE_MODIFICATOR);
    }

    public int getKilledMonstersAmount() {
        return monstersKilled.size;
    }
}