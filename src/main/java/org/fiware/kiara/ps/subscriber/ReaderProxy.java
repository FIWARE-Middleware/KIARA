package org.fiware.kiara.ps.subscriber;

import java.util.List;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterTimes;
import org.fiware.kiara.ps.rtps.common.ChangeForReader;
import org.fiware.kiara.ps.rtps.writer.StatefulWriter;

public class ReaderProxy {

    /**
     * Attributes of the Remote Reader
     */
    public final RemoteReaderAttributes att;

    /**
     * Constructor.
     *
     * @param rdata RemoteWriterAttributes to use in the creation.
     * @param times WriterTimes to use in the ReaderProxy.
     * @param SW Pointer to the StatefulWriter.
     */
    ReaderProxy(RemoteReaderAttributes rdata, WriterTimes times, StatefulWriter SW) {
        att = new RemoteReaderAttributes();
        att.copy(rdata);
    }

    /**
     * Get a vector of all unacked changes by this Reader.
     *
     * @param reqChanges Pointer to a vector of pointers.
     * @return True if correct.
     */
    public boolean unackedChanges(List<ChangeForReader> reqChanges) {
        throw new UnsupportedOperationException();
    }

    public void destroy() {

    }

}
