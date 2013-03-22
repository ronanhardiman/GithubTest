package com.igrs.tivic.phone.Interface;

import com.igrs.tivic.phone.Bean.LocationBean;
import com.igrs.tivic.phone.Bean.Base.BaseParamBean;
import com.igrs.tivic.phone.Bean.Base.PhotoListBean;
import com.igrs.tivic.phone.Bean.Login.UserCheckinBean;
import com.igrs.tivic.phone.Bean.Login.UserRegisterBean;

public interface IUser {
/*
 * 获取用户基本信息
 * user/get_info_base
{
    "uid": "1005",
    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
    "ver": 1,
    "req": {
        "uid": "1005"		//指定被查询的用户
    }
}
成功
{
    "ret": 0,
    "errcode": 0,
    "msg": "ok",
    "resp": {
        "info": {
            "relation": 0,		//0-陌生人，1-拉黑，2-粉丝，3-我关注对方，4-双向关注
            "nick": "ssslll",				// 最长16字
            "gender": "1",					// 0-女，1-男，2未知
            "birthday": "564334976",
            "sign": "newsign",				// 最长32字
            "loc": {						// 最近上报的LBS位置
                "lat": "39.9820579244",		// 纬度
                "lon": "116.302093086"		// 经度
            },
            "visit": {			//最近浏览栏目位置，新用户有可能为空
                "id": "",		//栏目ID
                "title": ""		//栏目名称
				"channelid": ""	//频道id（可选）
            },
            "last_time": ""		//最后上线时间戳
        }
    }
}
 */
	public UserRegisterBean getUsrBaseInfo(BaseParamBean bean);
	
/*
 * 获取用户扩展信息
 * user/get_info_ext
{
    "uid": "1005",
    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
    "ver": 1,
    "req": {
        "uid": "1005"		//指定被查询的用户
    }
}
成功
{
    "ret": 0,
    "errcode": 0,
    "msg": "ok",
    "resp": {
        "items": [
            {
                "icon": "",
                "text": "星座",
                "key": "astroid",
                "value": "7"			// 0(白羊)~11(双鱼)
            },
            {
                "icon": "",
                "text": "年代",
                "key": "age",
                "value": "564334976"	//birthday 1970-1-1偏移量
            },
            {
                "icon": "",
                "text": "性别",
                "key": "gender",
                "value": "1"			//0：woman；1：man
            },
            {
                "icon": "http://image.tivic.com/userdata/title/1352989398.png",
                "text": "明星",
                "key": "star_id",
                "value": "1"			//titleId
            },
            {
                "icon": "http://image.tivic.com/userdata/title/1352989419.png",
                "text": "节目",
                "key": "program_id",
                "value": "2"			//titleId
            },
            {
                "icon": "http://image.tivic.com/userdata/title/1352989514.png",
                "text": "内容",
                "key": "category_id",
                "value": "6"			//titleId
            },
            {
                "icon": "http://image.tivic.com/userdata/title/1353122653.jpg",
                "text": "内容",
                "key": "category_id",
                "value": "18"			//titleId
            },
            {
                "icon": "http://image.tivic.com/userdata/title/1353122720.jpg",
                "text": "内容",
                "key": "category_id",
                "value": "20"			//titleId
            }
        ]
    }
}	
 */
	public UserRegisterBean getUsrExtInfo(BaseParamBean bean);
	
/*
 * 修改用户信息
user/modify_info
{
	"uid": 1005
    "sid": "cfa629d6da75f8f5c01eae7a1576c3da",
    "ver": 1,
    "req": {
        "birthday": 564335000,
        "category": [
            6,
            18,
            20
        ],
        "gender": 1,
        "program": [
            2
        ],
        "star": [
            1
        ],
        "sign": "newsign"
    }
}
可以修改的项目：avatar,nick,birthday,gender,star,category,program,sign
在req节点内，至少添加以上一项。
修改头像时，avatar字段的值取非0即可，同时在json数据后追加file/base64	
 */
	public int modifyUsrInfo(BaseParamBean param, UserRegisterBean bean);
	
/*
 * 修改用户密码
user/modify_pwd
{
    "uid": "1005",
    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
    "ver": 1,
    "req": {
        "old": "password",
        "new": "apassword"
    }
}	
 */
	public int modifyUsrPassword(BaseParamBean param, String oldpwd, String newpwd);

/*
 * 上传用户图片
 * 返回值： 成功imageUrl 失败null
user/upload_photo
{
    "uid": "80",
    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
    "ver": 1,
}
json数据后追加file/base64
具体做法请参考1.1pad
成功
{
	"ret" :  0,
	"errcode" :  0,
	"msg" :  "ok",
	"resp" :  array(
		"url" :  "http://image.tivic.com/userdata/img/20121128/1354072898-80.jpg"
	)
}	
 */
	public String uploadPhoto(BaseParamBean param, String filepath);
	
/*
 * 获取用户照片列表（我或他人）
user/list_photo
{
    "uid": "80",
    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
    "ver": 1,	
	"req" :
	{
		"uid" : 80,							// 指定用户uid
		"page_no" : 0,						// 页号
		"per_page" : 20						// 每页条数
	}
}
成功
{
	"ret" :  0,
	"errcode" :  0,
	"msg" :  "ok",
	"resp" :
	{
		"more" : 0,							// 是否还有更多
		"results" : 
		[
			"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
			"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
			"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
			"url" : "http://xxx.com/imgs/20121128/1354072898-80.png",	// 上传后生成的URL
			....
		]
	}
}	
 */
	
	public PhotoListBean getPhotoList(BaseParamBean param);
	
/*
 * 更新用户坐标
user/update_location
{
    "uid": "80",
    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
    "ver": 1,	
	"req" :
	{
		"lat" : 1234.5678,		//纬度
		"lon" : 1234.5678		//经度
	}
}	
 */
	public int updateLocation(BaseParamBean param, LocationBean bean);
	
/*
 * 用户签到
 * checkin
{
    "uid": "80",
    "sid": "6e3926e2848760a3ea2fbb3ccee6d1ea",
    "ver": 1,
	"req" :
	{
		"photo" : 
		{
			"type" : 0,					// 0-POST附件,json数据后追加file/base64；1-图片URL
			"file_url" : "http://xx",	// 1-图片URL的情况下使用
		},
		"text" : "哈哈！TV客截屏，惊喜连连…",		// 用户选取的文字（可选），pad默认，用户不可修改
		"sync_sina" : 1,				// 转发新浪微博（可选）
	}
}	
 */
	public UserCheckinBean checkin(BaseParamBean param);
	
/*
 * 更新最近浏览节目位置
user/visit
{
    "sid": "ugqrcqu3pkh7tmln3pfa0147a6",
    "uid": 31,
    "ver": 1
    "req": {
        "visit": 647	//访问的节目ID
		"channelid": 10	//频道id（可选）
    },
}	
 */
	public int visit(BaseParamBean param, String channelid, String programid);
}
