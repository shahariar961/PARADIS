-module(barrier).
% -export(all).
-compile(export_all).

start(Expected) ->
    spawn_link(fun () -> barrier_loop(Expected, []) end).

barrier_loop([],Arrived) ->
    lists:foreach(
        fun({Pid,Ref})->
            Pid ! {continue,Ref}
        end,
        Arrived 
    );
% barrier_loop(Expected,Arrived) ->
%     receive
%         {arrive,Pid,Target,Ref}  when lists:member(Target,Expected) ->
%                 barrier_loop(fun()->lists:delete(Target,Expected)end,[{Pid,Ref} | Arrived]);
%         {arrive,Pid,_,Ref} ->
%                     Pid ! {continue,Ref}
%         end. 
barrier_loop(Expected,Arrived) ->
    receive
        {arrive,Pid,Target,Ref} ->
            case lists:member(Target,Expected) of
                true -> 
                barrier_loop(fun()->lists:delete(Target,Expected)end,[{Pid,Ref} | Arrived]);
                false->
                    Pid ! {continue,Ref}
                end
        end. 


wait(Barrier,Pid) ->
    Ref = make_ref(),
    Barrier ! {arrive, self(),Pid, Ref},
    receive
	{continue, Ref} ->
	    ok
    end.

	    
do_a() -> 
		io:format("Process A started~n").

do_more_a() -> 
		io:format("Process A resumed~n").
do_b() -> 
		io:format("Process B started~n").

do_more_b() -> 
		io:format("Process B Resumed~n").
do_c() -> 
		io:format("Process C started~n").

do_more_c() -> 
		io:format("Process C resumed~n").