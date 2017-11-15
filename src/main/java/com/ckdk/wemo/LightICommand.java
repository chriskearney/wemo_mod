package com.ckdk.wemo;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;

import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LightICommand extends CommandBase {

//    commands.light.failure=Unable to send message to Wemo device!
//    commands.light.success=Request sent to Wemo device.
//    commands.light.usage=/wemo "Wemo Friendly Name" on OR off

    private static final String KEARNEY_WEMO_CONTROL_SERVICE = "http://192.168.88.33:2017/";

    @Override
    public String getName() {
        return "light";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "wemo \"Wemo Friendly Name\" on OR off";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            sender.sendMessage(new TextComponentString("wemo \"Wemo Friendly Name\" on OR off"));
            return;
        }

        String rawCommand = StringUtils.join(args, " ");
        List<String> commandParts = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(rawCommand);
        while (m.find()) {
            commandParts.add(m.group(1).replace("\"", ""));
        }

        String friendlySwitchName = commandParts.get(0);
        String operation = commandParts.get(1);

        boolean operationResult = false;
        if (operation.equalsIgnoreCase("on")) {
            operationResult = turnOn(friendlySwitchName);
        } else if (operation.equalsIgnoreCase("off")) {
            operationResult = turnOff(friendlySwitchName);
        } else if (operation.equalsIgnoreCase("toggle")) {
            operationResult = toggle(friendlySwitchName);
        }
        if (!operationResult) {
            sender.sendMessage(new TextComponentString("Unable to send message to Wemo device!"));
            return;
        }

        sender.sendMessage(new TextComponentString("Request sent to Wemo device."));

    }

    private boolean turnOn(String switchFriendlyName) {
        try {

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(KEARNEY_WEMO_CONTROL_SERVICE + "wemo/" + URLEncoder.encode(switchFriendlyName, "ISO-8859-1").replace("+", "%20") + "/on");
            try (CloseableHttpResponse response = closeableHttpClient.execute(httpGet)) {
                return response.getStatusLine().getStatusCode() / 100 == 2;
            }
        } catch (Exception e) { }
        return false;
    }


    private boolean turnOff(String switchFriendlyName) {
        try {
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(KEARNEY_WEMO_CONTROL_SERVICE + "wemo/" + URLEncoder.encode(switchFriendlyName, "ISO-8859-1").replace("+", "%20") + "/off");
            try (CloseableHttpResponse response = closeableHttpClient.execute(httpGet)) {
                return response.getStatusLine().getStatusCode() / 100 == 2;
            }
        } catch (Exception e) { }
        return false;
    }

    private boolean toggle(String switchFriendlyName) {
        try {
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(KEARNEY_WEMO_CONTROL_SERVICE + "wemo/" + URLEncoder.encode(switchFriendlyName, "ISO-8859-1").replace("+", "%20") + "/toggle");
            try (CloseableHttpResponse response = closeableHttpClient.execute(httpGet)) {
                return response.getStatusLine().getStatusCode() / 100 == 2;
            }
        } catch (Exception e) { }
        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}