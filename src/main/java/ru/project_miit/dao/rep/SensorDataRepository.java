package ru.project_miit.dao.rep;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.project_miit.dao.entity.SensorData;

import java.util.Optional;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    Optional<SensorData> findTopByOrderByTimestampDesc();
}


