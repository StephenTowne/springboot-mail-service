package stephen.springboot.mailservice.common;

public interface Converter<T> {
    public abstract void doConvert(T t);
    public abstract T doReconvert();
}
