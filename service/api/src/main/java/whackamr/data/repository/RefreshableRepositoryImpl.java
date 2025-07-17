package whackamr.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RefreshableRepositoryImpl<T> implements RefreshableRepository<T>
{
    private EntityManager entityManager;

    @Override
    @Transactional
    public void refresh(T t)
    {
        entityManager.refresh(t);
    }
}
