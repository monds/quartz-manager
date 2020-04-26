package com.monds.quartzui.repository;

import com.monds.quartzui.entity.TriggerHistory;
import com.monds.quartzui.entity.TriggerHistoryPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryDefinition(domainClass = TriggerHistory.class, idClass = TriggerHistoryPK.class)
public interface TriggerHistoryRepository {

    Page<TriggerHistory> findByJobName(String jobName, Pageable pageable);

    TriggerHistory save(TriggerHistory triggerHistory);
}
