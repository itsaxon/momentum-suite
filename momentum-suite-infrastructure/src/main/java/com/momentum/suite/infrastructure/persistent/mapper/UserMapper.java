package com.momentum.suite.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.momentum.suite.infrastructure.persistent.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问接口 (Mapper)
 * <p>
 * {@code @Mapper}: 标记这是一个 MyBatis 的 Mapper 接口，会被 Spring 扫描并创建代理实现。
 * 继承 {@code BaseMapper<UserPO>} 后，自动获得了对 UserPO 的常用 CRUD 方法。
 *
 * @author itsaxon
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

    // 如果有复杂的、MyBatis-Plus 无法自动生成的 SQL，可以在这里定义方法，
    // 并在对应的 XML 文件中编写 SQL 语句。
    // 例如：
    // UserPO findByUsernameWithRoles(String username);

}
