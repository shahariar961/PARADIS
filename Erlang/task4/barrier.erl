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
                    NewList=lists:delete(Target,Expected),
                    barrier_loop(NewList,[{Pid,Ref} | Arrived]);
                % barrier_loop(fun()->lists:delete(Target,Expected)end,[{Pid,Ref} | Arrived]);
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

	    
do_a(Barrier,Ref) -> 
		io:format("Process A started~n"),
        barrier:wait(Barrier,Ref),
        io:format("Process A resumed~n").

do_more_a() -> 
		io:format("Process A resumed~n").
do_b(Barrier,Ref) -> 
		io:format("Process B started~n"),
        barrier:wait(Barrier,Ref),
        io:format("Process B resumed~n").

do_more_b() -> 
		io:format("Process B Resumed~n").
do_c(Barrier,Ref) -> 
		io:format("Process C started~n"),
        barrier:wait(Barrier,Ref),
        io:format("Process C resumed~n").

do_more_c() -> 
		io:format("Process C resumed~n").

list_test(Exp) ->
    io:format("List before deletion ~p~n",Exp).

start_test(Exp) ->
    spawn_link(fun () -> barrier_test(Exp) end).
barrier_test(Expected) ->
    receive
        {arrive,Pid,Target,Ref} ->
            case lists:member(Target,Expected) of
                
                true -> 
                    io:format("Deleted item ~p~n",[Expected]),
                    barrier_test(fun()->lists:delete(Target,Expected)end);
                % barrier_loop(fun()->lists:delete(Target,Expected)end,[{Pid,Ref} | Arrived]);
                false->
                    Pid ! {continue,Ref}
                end
        end. 