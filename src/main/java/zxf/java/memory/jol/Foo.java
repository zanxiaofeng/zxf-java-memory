package zxf.java.memory.jol;

import java.util.Arrays;
import java.util.List;

public class Foo {
    private byte b;
    private short s;
    private int i;
    private long l;
    private float f;
    private double d;
    private char c;
    private boolean bo;
    private String str;
    private Bar bar;

    public static Foo create(List<String> ids) {
        Foo foo = new Foo();
        foo.setB((byte) 123);
        foo.setS((short) 234);
        foo.setI(456);
        foo.setL(678l);
        foo.setF(12.34f);
        foo.setD(12.809888d);
        foo.setC('A');
        foo.setBo(true);
        foo.setStr("1234567890");
        foo.setBar(new Bar());
        foo.getBar().setCh('b');
        foo.getBar().setIds(ids);
        return foo;
    }

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public boolean isBo() {
        return bo;
    }

    public void setBo(boolean bo) {
        this.bo = bo;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public static class Bar {
        private Character ch;
        private List<String> ids;

        public Character getCh() {
            return ch;
        }

        public void setCh(Character ch) {
            this.ch = ch;
        }

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }
    }
}
