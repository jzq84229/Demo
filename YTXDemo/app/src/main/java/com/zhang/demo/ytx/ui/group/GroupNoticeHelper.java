package com.zhang.demo.ytx.ui.group;

import com.yuntongxun.ecsdk.im.ECGroup;
import com.zhang.demo.ytx.storage.ContactSqlManager;
import com.zhang.demo.ytx.storage.GroupSqlManager;
import com.zhang.demo.ytx.ui.contact.ECContacts;

/**
 * Created by Administrator on 2016/7/15.
 */
public class GroupNoticeHelper {
    private static GroupNoticeHelper mHelper;

    private GroupNoticeHelper(){}
    private static GroupNoticeHelper gethelper() {
        if (mHelper == null) {
            mHelper = new GroupNoticeHelper();
        }
        return mHelper;
    }

    /**
     * 替换content的联系人ID为联系人名称
     * @param content
     * @return
     */
    public static CharSequence getNoticeContent(String content) {
        if(content == null) {
            return content;
        }
        if(content.indexOf("<admin>") != -1 && content.indexOf("</admin>") != -1) {
            int start = content.indexOf("<admin>");
            int end = content.indexOf("</admin>");
            String contactId = content.substring(start + "<admin>".length(), end);
            ECContacts contact = ContactSqlManager.getContact(contactId);
            String target = content.substring(start, end + "</admin>".length());
            content = content.replace(target, contact.getNickname());
        }
        if(content.indexOf("<member>") != -1 && content.indexOf("</member>") != -1) {
            int start = content.indexOf("<member>");
            int end = content.indexOf("</member>");
            String member = content.substring(start + "<member>".length(), end);
            ECContacts contact = ContactSqlManager.getContact(member);
            String target = content.substring(start, end + "</member>".length());
            content = content.replace(target, contact.getNickname());
        }
        if(content.indexOf("<groupId>") != -1 && content.indexOf("</groupId>") != -1) {
            int start = content.indexOf("<groupId>");
            int end = content.indexOf("</groupId>");
            String groupId = content.substring(start + "<groupId>".length(), end);
            ECGroup ecGroup = GroupSqlManager.getECGroup(groupId);
            String target = content.substring(start, end + "</groupId>".length());
            if(ecGroup == null) {
                GroupService.syncGroupInfo(groupId);
            }
            content = content.replace(target, ecGroup!= null ? ecGroup.getName() : "");
        }
        return content;
    }

    /***********************************************************************************************/

}
