package fun.mooncraftgames.luna.astroitemlib.exceptions;

/**
 * (C) Copyright 2019 - Will Scully (CloudGamer360), All rights reserved
 *
 * By using the following application, you are accepting all responsibility
 * for any damage or legal issues which arise over it. Additionally, you are
 * agreeing not to use this application or it's components anywhere other
 * than the Mooncraft Minecraft Server unless you have written permission from
 * the copyright holder.
 *
 *
 * Failure to follow the license will result in a termination of license.
 */
public class InvalidItemException extends RuntimeException {

    public InvalidItemException(ExceptionType type, String item_id, String info) {
        super(getCustomizedMessage(type, item_id, info));
    }

    private static String getCustomizedMessage(ExceptionType type, String item_id, String info){
        String msg = "";
        switch (type){
            case ITEM_ID:
                msg = "Invalid item ID detected in template: "+item_id+" | "+info;
                break;
            case META:
                msg = "Invalid item meta for item detected in loot pool: "+item_id+" | "+info;
        }
        return msg;
    }

    public enum ExceptionType {
        ITEM_ID, META
    }

}
