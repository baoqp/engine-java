package com.alibabacloud.polar_race.engine.common;

import com.alibabacloud.polar_race.engine.base.Util;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.alibabacloud.polar_race.engine.rematch.GroupDB;

public class EngineRace extends AbstractEngine {
    //	private PreDB db;
//	private PreMemDB db;
    private GroupDB db;
//	private DB db;

    public static void main(String[] args) throws Exception {
        EngineRace engineRace = new EngineRace();
        String path = "C:\\temp\\kv";
        engineRace.open(path);
        String key = "hello";
        String value = "word";
        engineRace.write(key.getBytes(), value.getBytes());
        byte[] bytes = engineRace.read(key.getBytes());
        assert (new String(bytes)).equals(value);


        key = "123hello";
        value = "123word";
        engineRace.write(key.getBytes(), value.getBytes());
        bytes = engineRace.read(key.getBytes());
        assert (new String(bytes)).equals(value);
    }

    @Override
    public void open(String path) throws EngineException {
        try {
//			db = new PreMemDB(path);
            db = new GroupDB(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "open");
        }
    }

    @Override
    public void write(byte[] key, byte[] value) throws EngineException {
        try {
            db.write(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.CORRUPTION, "write");
        }
    }

    @Override
    public byte[] read(byte[] key) throws EngineException {
        byte[] value = null;
        try {
            value = db.read(key);
//			if(Util.checkAllZero(new Slice(value))) {
//				System.out.println("value 全0 key = " + Util.bytesToLong(key));
//			}
        } catch (Exception e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.NOT_FOUND, Util.getLittleEndianLong(key) + " read error " + e.getMessage());
        }
        if (value == null) {
            throw new EngineException(RetCodeEnum.NOT_FOUND, "read null");
        }
        return value;
    }

    @Override
    public void range(byte[] lower, byte[] upper, AbstractVisitor visitor) throws EngineException {
        try {
            db.range(lower, upper, visitor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.CORRUPTION, "range");
        }


    }

    @Override
    public void close() {
        try {
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
