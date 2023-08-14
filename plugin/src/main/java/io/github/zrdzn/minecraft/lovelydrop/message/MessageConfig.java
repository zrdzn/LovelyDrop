package io.github.zrdzn.minecraft.lovelydrop.message;

import eu.okaeri.configs.OkaeriConfig;
import io.github.zrdzn.minecraft.lovelydrop.serdes.ColoredText;

public class MessageConfig extends OkaeriConfig {

    private ColoredText executedAsConsole = new ColoredText("&cYou cannot execute this command as console.");

    private ColoredText noPermissions = new ColoredText("&cYou do not have enough permissions to perform that action.");

    private ColoredText menuOpenError = new ColoredText("&cSomething went wrong while opening the drop menu.");

    private ColoredText pluginReloaded = new ColoredText("&aPlugin has been reloaded.");

    private ColoredText notEnoughArguments = new ColoredText("&cYou need to provide arguments.");

    private ColoredText notValidArgument = new ColoredText("&cYou have provided wrong argument. &eUse /lovelydrop reload.");

    private ColoredText dropSwitched = new ColoredText("&aYou have switched the {DROP} drop.");

    private ColoredText dropSwitchedInventory = new ColoredText("&aYou have switched the {DROP} drop to inventory.");

    private ColoredText dropSuccessful = new ColoredText("&aYou have dropped {AMOUNT}x {DROP}!");

    private ColoredText needToJoinAgain = new ColoredText("&cSomething went wrong while loading your settings, please try joining again.");

    public ColoredText getExecutedAsConsole() {
        return this.executedAsConsole;
    }

    public void setExecutedAsConsole(ColoredText executedAsConsole) {
        this.executedAsConsole = executedAsConsole;
    }

    public ColoredText getNoPermissions() {
        return this.noPermissions;
    }

    public void setNoPermissions(ColoredText noPermissions) {
        this.noPermissions = noPermissions;
    }

    public ColoredText getMenuOpenError() {
        return this.menuOpenError;
    }

    public void setMenuOpenError(ColoredText menuOpenError) {
        this.menuOpenError = menuOpenError;
    }

    public ColoredText getPluginReloaded() {
        return this.pluginReloaded;
    }

    public void setPluginReloaded(ColoredText pluginReloaded) {
        this.pluginReloaded = pluginReloaded;
    }

    public ColoredText getNotEnoughArguments() {
        return this.notEnoughArguments;
    }

    public void setNotEnoughArguments(ColoredText notEnoughArguments) {
        this.notEnoughArguments = notEnoughArguments;
    }

    public ColoredText getNotValidArgument() {
        return this.notValidArgument;
    }

    public void setNotValidArgument(ColoredText notValidArgument) {
        this.notValidArgument = notValidArgument;
    }

    public ColoredText getDropSwitched() {
        return this.dropSwitched;
    }

    public void setDropSwitched(ColoredText dropSwitched) {
        this.dropSwitched = dropSwitched;
    }

    public ColoredText getDropSwitchedInventory() {
        return this.dropSwitchedInventory;
    }

    public void setDropSwitchedInventory(ColoredText dropSwitchedInventory) {
        this.dropSwitchedInventory = dropSwitchedInventory;
    }

    public ColoredText getDropSuccessful() {
        return this.dropSuccessful;
    }

    public void setDropSuccessful(ColoredText dropSuccessful) {
        this.dropSuccessful = dropSuccessful;
    }

    public ColoredText getNeedToJoinAgain() {
        return this.needToJoinAgain;
    }

    public void setNeedToJoinAgain(ColoredText needToJoinAgain) {
        this.needToJoinAgain = needToJoinAgain;
    }

}
