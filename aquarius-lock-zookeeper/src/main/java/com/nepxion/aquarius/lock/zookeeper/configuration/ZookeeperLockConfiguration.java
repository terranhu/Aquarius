package com.nepxion.aquarius.lock.zookeeper.configuration;

/**
 * <p>Title: Nepxion Aquarius</p>
 * <p>Description: Nepxion Aquarius</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.nepxion.aquarius.common.constant.AquariusConstant;
import com.nepxion.aquarius.common.curator.adapter.CuratorAdapter;
import com.nepxion.aquarius.common.curator.constant.CuratorConstant;
import com.nepxion.aquarius.common.curator.handler.CuratorHandler;
import com.nepxion.aquarius.common.curator.handler.CuratorHandlerImpl;
import com.nepxion.aquarius.lock.LockDelegate;
import com.nepxion.aquarius.lock.LockExecutor;
import com.nepxion.aquarius.lock.zookeeper.condition.ZookeeperLockCondition;
import com.nepxion.aquarius.lock.zookeeper.impl.ZookeeperLockDelegateImpl;
import com.nepxion.aquarius.lock.zookeeper.impl.ZookeeperLockExecutorImpl;

@Configuration
public class ZookeeperLockConfiguration {
    @Value("${" + CuratorConstant.CONFIG_PATH + ":" + CuratorConstant.DEFAULT_CONFIG_PATH + "}")
    private String curatorConfigPath;

    @Value("${" + AquariusConstant.PREFIX + "}")
    private String prefix;

    @Autowired(required = false)
    private CuratorAdapter curatorAdapter;

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public LockDelegate zookeeperLockDelegate() {
        return new ZookeeperLockDelegateImpl();
    }

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public LockExecutor<InterProcessMutex> zookeeperLockExecutor() {
        return new ZookeeperLockExecutorImpl();
    }

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public CuratorHandler curatorHandler() {
        if (curatorAdapter != null) {
            return curatorAdapter.getCuratorHandler(prefix);
        }

        return new CuratorHandlerImpl(curatorConfigPath, prefix);
    }
}