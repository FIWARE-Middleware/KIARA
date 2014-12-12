package org.fiware.kiara.server;

import org.fiware.kiara.server.Servant;
import java.util.List;

public interface Service {

    public void register(Object serviceImpl);

    public List<Servant> getGeneratedServants();
}
