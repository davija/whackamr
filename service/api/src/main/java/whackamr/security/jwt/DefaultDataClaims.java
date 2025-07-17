package whackamr.security.jwt;

import java.util.Map;

import io.jsonwebtoken.impl.DefaultClaims;

public class DefaultDataClaims extends DefaultClaims implements DataClaims
{
    public DefaultDataClaims()
    {
        super();
    }

    public DefaultDataClaims(Map<String, Object> map)
    {
        super(map);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m)
    {
        super.putAll(m);
    }

    @Override
    public boolean valid()
    {
        return true;
    }
}
