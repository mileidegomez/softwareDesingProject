
public aspect Screen 
{
	
	pointcut p(String output) : execution(* *.output(String)) && args(output);
	
	after(String output) : p(output)
	{
		System.out.println(output);
	}
	
}
