package io.jingwei.registry;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.rocksdb.*;

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
        RocksDB.loadLibrary();
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

    public static void main(String[] args) throws RocksDBException {
        LoadData test = new LoadData();
       //test.testDefaultColumnFamily();
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
            System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
            String hex = HexBin.encode(iter.value());
            System.out.println(new String(HexBin.decode(hex)));

            Libp2PPeer.signed_peer peer = Libp2PPeer.signed_peer.parseFrom(iter.value());
            System.out.println(peer);
            i ++;
        }

        System.out.println("count=" + i);

//        rocksDB.remove(key);
//        System.out.println("after remove key:" + new String(key));
//
//        iter = rocksDB.newIterator();
//        for(iter.seekToFirst(); iter.isValid(); iter.next()) {
//            System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
//        }

    }
}
