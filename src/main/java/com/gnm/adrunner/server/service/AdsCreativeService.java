package com.gnm.adrunner.server.service;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import com.gnm.adrunner.server.entity.AdsCreative;
import org.springframework.stereotype.Service;

@Service("AdsCreativeService")
public class AdsCreativeService {

    @PersistenceContext
	private EntityManager entityManager;

    public void update(Integer fileIndex, String extension, String adsKey) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<AdsCreative> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(AdsCreative.class);
        Root<AdsCreative> root = criteriaUpdate.from(AdsCreative.class);
        criteriaUpdate.set("ext"+fileIndex,      "extension");
        criteriaUpdate.where(criteriaBuilder.equal(root.get("adsKey"), adsKey));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }
    
}
