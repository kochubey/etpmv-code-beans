package etpmv.system;

public class Unpacker extends Transformer {
    public Unpacker(Data data, String subscriber) {
        super(data, subscriber);
    }

    @Override
    public String path() {
        if (data().isBroadcast()) return data().path() + "/" + data().subscriber() + "/unpack.xsl";
        if (data().isAck()) return data().path() + "/unpack.xsl";
        if (data().isRequest()) return data().path() + "/unpack.xsl";
        if (data().isResponse()) return data().path() + "/" + data().subscriber() + "/unpack.xsl";
        return null;
    }
}
