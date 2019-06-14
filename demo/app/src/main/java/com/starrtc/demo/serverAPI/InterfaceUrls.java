package com.starrtc.demo.serverAPI;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.starrtc.demo.database.HistoryBean;
import com.starrtc.demo.demo.MLOC;
import com.starrtc.demo.demo.audiolive.AudioLiveInfo;
import com.starrtc.demo.demo.im.chatroom.ChatroomInfo;
import com.starrtc.demo.demo.im.group.MessageGroupInfo;
import com.starrtc.demo.demo.miniclass.MiniClassInfo;
import com.starrtc.demo.demo.superroom.SuperRoomInfo;
import com.starrtc.demo.demo.thirdstream.RtspInfo;
import com.starrtc.demo.demo.videolive.LiveInfo;
import com.starrtc.demo.demo.videomeeting.MeetingInfo;
import com.starrtc.demo.utils.AEvent;
import com.starrtc.demo.utils.ICallback;
import com.starrtc.demo.utils.StarHttpUtil;

/**
 * Created by zhangjt on 2017/8/17.
 */

public class InterfaceUrls {
    public static String BASE_URL;
    //获取authKey
    public static String LOGIN_URL;
    //会议室列表
    public static String MEETING_LIST_URL;
    //直播列表
    public static String LIVE_LIST_URL;
    //音频直播列表
    public static String AUDIO_LIVE_LIST_URL;
    //音频直播列表
    public static String SUPER_ROOM_LIST_URL;
    //小班课列表
    public static String MINI_CLASS_LIST_URL;
    //上报直播间使用的聊天室ID（直播里的文字聊天用了一个聊天室）
    public static String LIVE_SET_CHAT_URL;
    //聊天室列表
    public static String CHATROOM_LIST_URL;
    //自己加入的群列表
    public static String GROUP_LIST_URL;
    //群成员列表
    public static String GROUP_MEMBERS_URL;
    //在线用户列表
    public static String ONLINE_USER_LIST_URL;

    //上报直播
    public static String REPORT_LIVE_INFO_URL;
    //上报语音直播
    public static String REPORT_AUDIO_LIVE_INFO_URL;
    //上报语音直播
    public static String REPORT_SUPER_ROOM_INFO_URL;
    //上报小班课
    public static String REPORT_MINI_CLASS_INFO_URL;
    //上报会议
    public static String REPORT_MEETING_INFO_URL;
    //上报聊天室
    public static String REPORT_CHATROOM_INFO_URL;

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
        LOGIN_URL = BASE_URL+"/authKey";
        MEETING_LIST_URL = BASE_URL+"/meeting/list";
        LIVE_LIST_URL = BASE_URL+"/live/list";
        AUDIO_LIVE_LIST_URL = BASE_URL+"/audio/list";
        SUPER_ROOM_LIST_URL = BASE_URL+"/super_room/list";
        MINI_CLASS_LIST_URL = BASE_URL+"/class/list";
        LIVE_SET_CHAT_URL = BASE_URL+"/live/set_chat";
        CHATROOM_LIST_URL = BASE_URL+"/chat/list";
        GROUP_LIST_URL = BASE_URL+"/group/list_all";
        ONLINE_USER_LIST_URL = BASE_URL+"/user/list";
        GROUP_MEMBERS_URL = BASE_URL+"/group/members";
        REPORT_LIVE_INFO_URL = BASE_URL+"/live/store";
        REPORT_AUDIO_LIVE_INFO_URL = BASE_URL+"/audio/store";
        REPORT_SUPER_ROOM_INFO_URL = BASE_URL+"/super_room/store";
        REPORT_MINI_CLASS_INFO_URL = BASE_URL+"/class/store";
        REPORT_MEETING_INFO_URL = BASE_URL+"/meeting/store";
        REPORT_CHATROOM_INFO_URL = BASE_URL+"/chat/store";
    }

    public static void demoLogin(String userId){
        String url = LOGIN_URL+"?userid="+userId+"&appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("starUid",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        MLOC.authKey = data;
                        AEvent.notifyListener(AEvent.AEVENT_LOGIN,true,"登录成功");
                        return;
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_LOGIN,false,"登录失败！");
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //会议室列表
    public static void demoRequestMeetingList(){
        String url = MEETING_LIST_URL+"?appid="+MLOC.agentId;
        String params = "";
        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<MeetingInfo> res = new ArrayList<MeetingInfo>();
                            for (int i = 0;i<datas.length();i++){
                                MeetingInfo meetingInfo = new MeetingInfo();
                                meetingInfo.createrId = datas.getJSONObject(i).getString("Creator");
                                meetingInfo.meetingName = datas.getJSONObject(i).getString("Name");
                                meetingInfo.meetingId = datas.getJSONObject(i).getString("ID");
                                res.add(meetingInfo);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_MEETING_GOT_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_MEETING_GOT_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_MEETING_GOT_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //小班课列表
    public static void demoRequestMiniClassList(){
        String url = MINI_CLASS_LIST_URL+"?appid="+MLOC.agentId;
        String params = "";
        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<MiniClassInfo> res = new ArrayList<MiniClassInfo>();
                            for (int i = 0;i<datas.length();i++){
                                MiniClassInfo meetingInfo = new MiniClassInfo();
                                meetingInfo.creator = datas.getJSONObject(i).getString("Creator");
                                meetingInfo.name = datas.getJSONObject(i).getString("Name");
                                meetingInfo.id = datas.getJSONObject(i).getString("ID");
                                res.add(meetingInfo);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_MINI_CLASS_GOT_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_MINI_CLASS_GOT_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_MINI_CLASS_GOT_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //聊天室列表
    public static void demoRequestChatroomList(){
        String url = CHATROOM_LIST_URL+"?appid="+MLOC.agentId;
        String params = "";
        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<ChatroomInfo> res = new ArrayList<ChatroomInfo>();
                            for (int i = 0;i<datas.length();i++){
                                ChatroomInfo chatroomInfo = new ChatroomInfo();
                                chatroomInfo.createrId = datas.getJSONObject(i).getString("Creator");
                                chatroomInfo.roomId = datas.getJSONObject(i).getString("ID");
                                chatroomInfo.roomName = datas.getJSONObject(i).getString("Name");
                                res.add(chatroomInfo);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_CHATROOM_GOT_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_CHATROOM_GOT_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_CHATROOM_GOT_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //群列表
    public static void demoRequestGroupList(String userid){
        String url = GROUP_LIST_URL+"?userid="+userid+"&appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid",userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    //{"status":1,"data":[{"roomId":"meeting_demo93","channelId":"10Ko5gjDr@3eaaGb","userId":"demo92"},{"roomId":"meeting_demo92","channelId":"10Ko5gjDr@3eaaGa","userId":"demo92"}]}
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<MessageGroupInfo> res = new ArrayList<MessageGroupInfo>();
                            for (int i = 0;i<datas.length();i++){
                                MessageGroupInfo groupInfo = new MessageGroupInfo();
                                groupInfo.createrId = datas.getJSONObject(i).getString("creator");
                                groupInfo.groupId = datas.getJSONObject(i).getString("groupId");
                                groupInfo.groupName = datas.getJSONObject(i).getString("groupName");
                                res.add(groupInfo);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_GROUP_GOT_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_GROUP_GOT_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_GROUP_GOT_LIST,false,"数据解析失败");
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //群成员列表
    public static void demoRequestGroupMembers(String groupId){
        String url = GROUP_MEMBERS_URL+"?groupId="+groupId+"&appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("groupId",groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<String> res = new ArrayList<String>();
                            for (int i = 0;i<datas.length();i++){
                                String uid = datas.getJSONObject(i).getString("userId");
                                res.add(uid);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_GROUP_GOT_MEMBER_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_GROUP_GOT_MEMBER_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_GROUP_GOT_MEMBER_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //在线用户列表
    public static void demoRequestOnlineUsers(){
        String url = ONLINE_USER_LIST_URL+"?appid="+MLOC.agentId;
        String params = "";
        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<HistoryBean> res = new ArrayList<HistoryBean>();
                            for (int i = 0;i<datas.length();i++){
                                String uid = datas.getJSONObject(i).getString("userId");
                                HistoryBean bean = new HistoryBean();
                                bean.setConversationId(uid);
                                res.add(bean);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_GOT_ONLINE_USER_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_GOT_ONLINE_USER_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_GOT_ONLINE_USER_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //互动直播列表
    public static void demoRequestLiveList(){
        String url = LIVE_LIST_URL+"?appid="+MLOC.agentId;
        String params = "";
        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<LiveInfo> res = new ArrayList<LiveInfo>();
                            for (int i = 0;i<datas.length();i++){
                                LiveInfo videoLiveInfo = new LiveInfo();
                                videoLiveInfo.createrId = datas.getJSONObject(i).getString("Creator");
                                videoLiveInfo.liveName = datas.getJSONObject(i).getString("Name");
                                videoLiveInfo.liveId = datas.getJSONObject(i).getString("ID");
                                videoLiveInfo.isLiveOn = datas.getJSONObject(i).getString("liveState");
                                res.add(videoLiveInfo);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_LIVE_GOT_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_LIVE_GOT_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_LIVE_GOT_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //音频直播列表
    public static void demoRequestAudioLiveList(){
        String url = AUDIO_LIVE_LIST_URL+"?appid="+MLOC.agentId;
        String params = "";

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<AudioLiveInfo> res = new ArrayList<AudioLiveInfo>();
                            for (int i = 0;i<datas.length();i++){
                                AudioLiveInfo videoLiveInfo = new AudioLiveInfo();
                                videoLiveInfo.creator = datas.getJSONObject(i).getString("Creator");
                                videoLiveInfo.name = datas.getJSONObject(i).getString("Name");
                                videoLiveInfo.id = datas.getJSONObject(i).getString("ID");
                                videoLiveInfo.isLiveOn = datas.getJSONObject(i).getString("liveState");
                                res.add(videoLiveInfo);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_AUDIO_LIVE_GOT_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_AUDIO_LIVE_GOT_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_AUDIO_LIVE_GOT_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //音频直播列表
    public static void demoRequestSuperRoomList(){
        String url = SUPER_ROOM_LIST_URL+"?appid="+MLOC.agentId;
        String params = "";

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    if(statusCode.equals("1")){
                        try {
                            JSONArray datas = new JSONArray(data);
                            ArrayList<SuperRoomInfo> res = new ArrayList<SuperRoomInfo>();
                            for (int i = 0;i<datas.length();i++){
                                SuperRoomInfo videoLiveInfo = new SuperRoomInfo();
                                videoLiveInfo.creator = datas.getJSONObject(i).getString("Creator");
                                videoLiveInfo.name = datas.getJSONObject(i).getString("Name");
                                videoLiveInfo.id = datas.getJSONObject(i).getString("ID");
                                videoLiveInfo.isLiveOn = datas.getJSONObject(i).getString("liveState");
                                res.add(videoLiveInfo);
                            }
                            AEvent.notifyListener(AEvent.AEVENT_SUPER_ROOM_GOT_LIST,true,res);
                            return;
                        } catch (JSONException e) {
                            AEvent.notifyListener(AEvent.AEVENT_SUPER_ROOM_GOT_LIST,false,"数据解析失败");
                            e.printStackTrace();
                        }
                    }
                }
                AEvent.notifyListener(AEvent.AEVENT_SUPER_ROOM_GOT_LIST,false,"数据解析失败");

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //互动直播
    public static void demoReportLive(String liveID,String liveName,String creatorID){
        String url = REPORT_LIVE_INFO_URL+"?appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",liveID);
            jsonObject.put("Name",liveName);
            jsonObject.put("Creator",creatorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_POST);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //上报语音直播
    public static void demoReportAudioLive(String liveID,String liveName,String creatorID){
        String url = REPORT_AUDIO_LIVE_INFO_URL+"?appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",liveID);
            jsonObject.put("Name",liveName);
            jsonObject.put("Creator",creatorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_POST);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //上报语音直播
    public static void demoReportSuperRoom(String liveID,String liveName,String creatorID){
        String url = REPORT_SUPER_ROOM_INFO_URL+"?appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",liveID);
            jsonObject.put("Name",liveName);
            jsonObject.put("Creator",creatorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_POST);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //上报小班课
    public static void demoReportMiniClass(String liveID,String liveName,String creatorID){
        String url = REPORT_MINI_CLASS_INFO_URL+"?appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",liveID);
            jsonObject.put("Name",liveName);
            jsonObject.put("Creator",creatorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_POST);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //会议室
    public static void demoReportMeeting(String liveID,String liveName,String creatorID){
        String url = REPORT_MEETING_INFO_URL+"?appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",liveID);
            jsonObject.put("Name",liveName);
            jsonObject.put("Creator",creatorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_POST);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //聊天室
    public static void demoReportChatroom(String liveID,String liveName,String creatorID){
        String url = REPORT_CHATROOM_INFO_URL+"?appid="+MLOC.agentId;
        String params = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",liveID);
            jsonObject.put("Name",liveName);
            jsonObject.put("Creator",creatorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = jsonObject.toString();

        StarHttpUtil httpPost = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_POST);
        httpPost.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //推送rtsp流
    public static void demoRequestPushList(){
        String url = "https://api.starrtc.com/public/user/stream?appid="+MLOC.agentId+"&userid="+MLOC.userId;
        String params = "";
        StarHttpUtil httpGet = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpGet.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    JSONArray datas = null;
                    try {
                        datas = new JSONArray(data);
                        ArrayList<RtspInfo> res = new ArrayList<RtspInfo>();
                        for (int i = 0; i < datas.length(); i++) {
                            RtspInfo rtspInfo = new RtspInfo();
                            rtspInfo.creator = datas.getJSONObject(i).getString("Creator");
                            rtspInfo.name = datas.getJSONObject(i).getString("Name");
                            rtspInfo.id = datas.getJSONObject(i).getString("ID");
                            rtspInfo.isLiveOn = datas.getJSONObject(i).getString("liveState");
                            res.add(rtspInfo);
                        }
                        AEvent.notifyListener(AEvent.AEVENT_RTSP_GOT_LIST,true,res);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AEvent.notifyListener(AEvent.AEVENT_RTSP_GOT_LIST,false,statusCode+" "+data);
                    }

                }else{
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_GOT_LIST,false,statusCode+" "+data);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpGet.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //转发rtsp流
    public static void demoPushRtsp(String server,String name,String chatroomId,int listtype,String rtspUrl){
        String url = "http://"+server+"/push?streamType=rtsp&streamUrl="+rtspUrl+"&roomLiveType="+listtype+"&roomId="+chatroomId+"&extra="+name;
        String params = "";
        StarHttpUtil httpGet = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpGet.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_FORWARD,true,data);
                }else{
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_FORWARD,false,statusCode+" "+data);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpGet.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }
    //恢复转发rtsp流 //rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov
    public static void demoResumePushRtsp(String server,String liveId,String rtsp){
        String url = "http://"+server+"/push?streamType=rtsp&streamUrl="+rtsp+"&channelId="+liveId.substring(0,16);
        String params = "";
        StarHttpUtil httpGet = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpGet.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_RESUME,true,null);
                }else{
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_RESUME,false,statusCode+" "+data);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpGet.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //停止转发rtsp流 //aisee.f3322.org:19932
    public static void demoStopPushRtsp(String server,String liveId){
        String url = "http://"+server+"/close?channelId="+liveId.substring(0,16);
        String params = "";
        StarHttpUtil httpGet = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpGet.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_STOP,true,null);
                }else{
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_STOP,false,statusCode+" "+data);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpGet.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }

    //删除rtsp流记录
    public static void demoDeleteRtsp(String server,String liveId){
        String url = "http://"+server+"/delete?channelId="+liveId.substring(0,16);
        String params = "";
        StarHttpUtil httpGet = new StarHttpUtil(StarHttpUtil.REQUEST_METHOD_GET);
        httpGet.addListener(new ICallback() {
            @Override
            public void callback(boolean reqSuccess, String statusCode, String data) {
                if(reqSuccess){
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_DELETE,true,null);
                }else{
                    AEvent.notifyListener(AEvent.AEVENT_RTSP_DELETE,false,statusCode+" "+data);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(StarHttpUtil.URL,url);
        bundle.putString(StarHttpUtil.DATA,params);
        httpGet.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,bundle);
    }


}
