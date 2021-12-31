package io.jingwei.registry;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.rocksdb.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className: LoadData
 * @description: TODO 类描述
 * @author: andy
 * @date: 12/28/21
 **/
public class LoadData {
    static {
//        RocksDB.loadLibrary();
    }

    private static final String  dbPath = "/opt/miner_data/blockchain_swarm/peerbook";
//    private static final String  dbPath = "/Users/fanyongpeng/hnt/data/rocksDB";
    RocksDB rocksDB;


    public LoadData() throws RocksDBException {

    }

    //  RocksDB.DEFAULT_COLUMN_FAMILY
    public void testDefaultColumnFamily() throws RocksDBException {
        Options options = new Options();
        options.setCreateIfMissing(true);

        rocksDB = RocksDB.open(options, dbPath);
//        byte[] key = "Hello".getBytes();
//        byte[] value = "World".getBytes();
//        rocksDB.put(key, value);
//
//        List<byte[]> cfs = RocksDB.listColumnFamilies(options, dbPath);
//        for(byte[] cf : cfs) {
//            System.out.println("AAA: "+new String(cf));
//        }
//
//        byte[] getValue = rocksDB.get(key);
//        System.out.println(new String(getValue));
//
//        rocksDB.put("SecondKey".getBytes(), "SecondValue".getBytes());
//
//        List<byte[]> keys = new ArrayList<>();
//        keys.add(key);
//        keys.add("SecondKey".getBytes());
//
//        Map<byte[], byte[]> valueMap = rocksDB.multiGet(keys);
//        for(Map.Entry<byte[], byte[]> entry : valueMap.entrySet()) {
//            System.out.println(new String(entry.getKey()) + ":" + new String(entry.getValue()));
//        }
//
//        RocksIterator iter = rocksDB.newIterator();
//        for(iter.seekToFirst(); iter.isValid(); iter.next()) {
//            System.out.println("iter keybbbb:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
//        }
//
//        rocksDB.remove(key);
//        System.out.println("after remove key:" + new String(key));



        rocksDB.put("Hello".getBytes(), "World".getBytes());


        byte[] key = "Hello".getBytes();
        WriteBatch batch = new WriteBatch();
        WriteOptions op = new WriteOptions();
        op.setSync(true);

        //batch.delete(key);
        batch.put("SecondKey".getBytes(), "SecondValue".getBytes());

        rocksDB.write(op, batch);

        RocksIterator iter = rocksDB.newIterator();
        //iter = rocksDB.newIterator();
        for(iter.seekToFirst(); iter.isValid(); iter.next()) {
            System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));

        }

        iter.close();

    }

    public static void testPub() throws Exception{
        String addr = "11wtAzwYQaCJ9ptxtC2iQskDsQDhAnBNkqybpkNk3azpYg2AhTQ";
        byte[] res = Base58Util.decode(addr);
        byte[] pub = new byte[res.length-5];
        byte[] vPayload = new byte[res.length-4];
        System.arraycopy(res, 0, vPayload, 0, vPayload.length);
        byte[] checksum = getSHA(getSHA(vPayload));
        System.out.println(res);

    }

    public static void testProto() {
        try {
//            testPub();
            String hex = "0ADB030A21006899EDF8F00D0539E5599DF7BB5791B5ED6CB38A0A9C2D332ED9F29F2102000012080446AD28BA06AC7E1A2100AF1A7961E5594B47367BCA158377FCC7E618E6B4E1B5373B7019B2FA291F22311A210014BC5C49BB6348CA5CCF67B1EB670BE906B82EC406E60838DFF5F787B5D9F87B1A2100E216FA6D8A1973B13ACBFE414CA81DC4F0B335221739DE9794B8505D15D63E9D1A2100291C5EBBB631C37C4C3E17DDD83B156D22B52B2781FC5374922E00C1C38B16FB1A21008A83F90D9BADC9BEBAFD487CE6E89814E2AFC22030705CB26DFC552D5C867C001A21008547AF776C09C24DE7F0A7E97B9917D8251B4686542AD5A2589BBA342042DCCF1A2100FCA6FFEBDB9EAE841F5C9CCC885747961F2483CD30B80E0E64AF7FE0A9E2E7FC1A210035F324C5184FC2B6214CBFFC6CA603C6B606FCCF153789F8C0B9DFFB46BF42DF1A2100B0DC0E565ADC5D4E4E0ADE80BB8746CBEE1A3AC6336D351D756F4D20D7567E5D288AD5D4BDE02F3A202DAE8FB95F343ECF6A4CFD37DFD3F09BAE2ADFF75E31392FE893A8D76E751092421D0A136C6173745F626C6F636B5F6164645F74696D65120608A8F0B28E0642190A0C72656C656173655F696E666F12091A07756E6B6E6F776E420E0A0668656967687412040888DA4612473045022100A934C30BDE8C991DDA100DCA7FA0CE5CC1EA19D5A19AB917719EA6DC16208FCD02201EADAA8166E04B51B7804A4351CCD6DB8314D4C7F8FE7631222B61281A24B7AC3AC1CC61";

            byte[] bytes = HexBin.decode(hex);
            System.out.println(new String(bytes));
            Libp2PPeer.signed_peer peer = Libp2PPeer.signed_peer.parseFrom(bytes);
//            peer.getPeer().getPubkey()
            byte[] pubkey = peer.getPeer().getPubkey().toByteArray();
            byte[] vPayload = new byte[pubkey.length+1];
            vPayload[0]=0;
            System.arraycopy(pubkey, 0, vPayload, 1, pubkey.length);
            byte[] checksum = getSHA(getSHA(vPayload));
            byte[] res = new byte[1+pubkey.length+4];
            res[0]=0;
            System.arraycopy(pubkey, 0, res, 1, pubkey.length);
            System.arraycopy(checksum, 0, res, pubkey.length+1, 4);


            System.out.println(Base58Util.encode(peer.getPeer().getPubkey().toByteArray()));
            System.out.println(Base58Util.encode(res));


            System.out.println(peer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getSHA(byte[] input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input);
    }


    public static void main(String[] args) throws RocksDBException {
        LoadData test = new LoadData();
       //test.testDefaultColumnFamily();
        testProto();
        try {
            test.testCertainColumnFamily2();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


    public void testCertainColumnFamily2() throws RocksDBException, IllegalAccessException, InstantiationException, InvalidProtocolBufferException {
        Options options = new Options();
        options.setCreateIfMissing(true);

        rocksDB = RocksDB.open(options, dbPath);
//        byte[] key = "Hello".getBytes();
//        byte[] value = "World".getBytes();
//        rocksDB.put(key, value);
//
//        List<byte[]> cfs = RocksDB.listColumnFamilies(options, dbPath);
//        for(byte[] cf : cfs) {
//            System.out.println("AAA: "+new String(cf));
//        }
//
//        byte[] getValue = rocksDB.get(key);
//        System.out.println(new String(getValue));
//
//        rocksDB.put("SecondKey".getBytes(), "SecondValue".getBytes());
//
//        List<byte[]> keys = new ArrayList<>();
//        keys.add(key);
//        keys.add("SecondKey".getBytes());
//
//        Map<byte[], byte[]> valueMap = rocksDB.multiGet(keys);
//        for(Map.Entry<byte[], byte[]> entry : valueMap.entrySet()) {
//            System.out.println(new String(entry.getKey()) + ":" + new String(entry.getValue()));
//        }

        ReadOptions readOptions = new ReadOptions();
        readOptions.setFillCache(false);
        RocksIterator iter = rocksDB.newIterator();
        int i = 0;

        for(iter.seekToFirst(); iter.isValid(); iter.next()) {
            try {
//                System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
//                String hex = HexBin.encode(iter.value());
//                System.out.println("hex: "+hex);
//                System.out.println(new String(HexBin.decode(hex)));

                Libp2PPeer.signed_peer peer = Libp2PPeer.signed_peer.parseFrom(iter.value());

                byte[] pubkey = peer.getPeer().getPubkey().toByteArray();
                byte[] vPayload = new byte[pubkey.length+1];
                vPayload[0]=0;
                System.arraycopy(pubkey, 0, vPayload, 1, pubkey.length);
                byte[] checksum = getSHA(getSHA(vPayload));
                byte[] res = new byte[1+pubkey.length+4];
                res[0]=0;
                System.arraycopy(pubkey, 0, res, 1, pubkey.length);
                System.arraycopy(checksum, 0, res, pubkey.length+1, 4);
//                System.out.println(Base58Util.encode(res));
                System.out.println(peer);
            } catch (Exception e) {
//                e.printStackTrace();
            }

            i ++;
        }

//        System.out.println("count=" + i);

//        rocksDB.remove(key);
//        System.out.println("after remove key:" + new String(key));
//
//        iter = rocksDB.newIterator();
//        for(iter.seekToFirst(); iter.isValid(); iter.next()) {
//            System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
//        }

    }
}
