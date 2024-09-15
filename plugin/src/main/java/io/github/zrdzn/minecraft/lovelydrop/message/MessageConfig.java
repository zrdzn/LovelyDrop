package io.github.zrdzn.minecraft.lovelydrop.message;

import eu.okaeri.configs.OkaeriConfig;
import io.github.zrdzn.minecraft.lovelydrop.serdes.ColoredText;

public class MessageConfig extends OkaeriConfig {

    public ColoredText executedAsConsole = new ColoredText("&cYou cannot execute this command as console.");
    public ColoredText noPermissions = new ColoredText("&cYou do not have enough permissions to perform that action.");
    public ColoredText menuOpenError = new ColoredText("&cSomething went wrong while opening the drop menu.");
    public ColoredText pluginReloaded = new ColoredText("&aPlugin has been reloaded.");
    public ColoredText notEnoughArguments = new ColoredText("&cYou need to provide arguments.");
    public ColoredText notValidArgument = new ColoredText("&cYou have provided wrong argument. &eUse /lovelydrop reload.");
    public ColoredText dropSwitched = new ColoredText("&aYou have switched the {DROP} drop.");
    public ColoredText dropSwitchedInventory = new ColoredText("&aYou have switched the {DROP} drop to inventory.");
    public ColoredText dropSuccessful = new ColoredText("&aYou have dropped {AMOUNT}x {DROP}!");
    public ColoredText needToJoinAgain = new ColoredText("&cSomething went wrong while loading your settings, please try joining again.");

}
