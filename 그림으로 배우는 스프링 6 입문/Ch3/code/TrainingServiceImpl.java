package Ch3.code;

import java.util.List;

public class TrainingServiceImpl implements TrainingService {
    private TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public List<Training> findAll() {
        return trainingRepository.selectAll();
    }
}
