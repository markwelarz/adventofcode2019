package advent.intcode;

public interface IntcodeComputerListener
{
	void onMessage(String message);

	void finished();
}
