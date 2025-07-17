package whackamr.data.repository;

public interface RefreshableRepository<T>
{
    void refresh(T t);
}
