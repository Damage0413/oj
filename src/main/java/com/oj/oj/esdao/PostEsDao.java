package com.oj.oj.esdao;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.oj.oj.model.dto.post.PostEsDTO;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

    List<PostEsDTO> findByUserId(Long userId);
}