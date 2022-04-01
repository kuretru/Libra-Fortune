package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户表
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("user")
public class UserDO extends BaseDO {

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 绑定的双子身份验证中心用户ID */
    private String geminiId;

}
