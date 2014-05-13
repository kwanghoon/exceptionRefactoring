package android.app;

public interface Catch {
	boolean handle(Throwable t) throws Throwable; 
}
