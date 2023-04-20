package com.freeing.unqid.leafcore.dao.impl;

import com.freeing.unqid.leafcore.dao.LeafAllocDao;
import com.freeing.unqid.leafcore.dao.LeafAllocMapper;
import com.freeing.unqid.leafcore.segment.model.entity.LeafAlloc;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author yanggy
 */
public class LeafAllocDaoImpl implements LeafAllocDao {

    /**
     * mybatis 的 session 工厂
     */
    private final SqlSessionFactory sqlSessionFactory;

    public LeafAllocDaoImpl(DataSource dataSource) {
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, dataSource);
            // mybatis 配置
            Configuration configuration = new Configuration(environment);
            configuration.addMapper(LeafAllocMapper.class);
            // 通过配置创建 sqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Override
    public List<LeafAlloc> getAllLeafAllocs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
            return sqlSession.getMapper(LeafAllocMapper.class).getAllLeafAllocs();
        }
    }

    @Override
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            LeafAllocMapper mapper = sqlSession.getMapper(LeafAllocMapper.class);
            mapper.updateMaxId(tag);
            LeafAlloc result = mapper.getLeafAlloc(tag);
            // 提交事务
            sqlSession.commit();
            return result;
        }
    }

    @Override
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            LeafAllocMapper mapper = sqlSession.getMapper(LeafAllocMapper.class);
            mapper.updateMaxIdByCustomStep(leafAlloc);
            LeafAlloc result = mapper.getLeafAlloc(leafAlloc.getKey());
            sqlSession.commit();
            return result;
        }
    }

    @Override
    public List<String> getAllTags() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
            LeafAllocMapper mapper = sqlSession.getMapper(LeafAllocMapper.class);
            return mapper.getAllTags();
        }
    }
}
