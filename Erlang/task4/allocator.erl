-module(allocator).

-export([start/1, request/2, release/2, test2/0, allocate_test/3]).

start(Resources) ->
    spawn_link(fun () ->
		       allocator(Resources)
	       end).

request(Pid, N) ->
    Ref = make_ref(),
    Pid ! {request, {self(), Ref, N}},
    receive
	{granted, Ref, Granted} ->
	    Granted
    end.

release(Pid, Resources) ->
    Ref = make_ref(),
    Pid ! {release, {self(), Ref, Resources}},
    receive
	{released, Ref} ->
	    ok
    end.


allocator(Resources) ->
    receive
	{request, {Pid, Ref, N}} when N =< length(Resources) ->
	    {G, R} = lists:split(N, Resources),
	    Pid ! {granted, Ref, G},
	    allocator(R);
	{release, {Pid, Ref, Released}} ->
	    Pid ! {released, Ref},
	    allocator(Released ++ Resources)
    end.
			     
test2() ->
    Allocator = allocator:start([a,b,c,d]),
    spawn(?MODULE, allocate_test, [Allocator, "Process A", 2]),
    spawn(?MODULE, allocate_test, [Allocator, "Process B", 2]),
    spawn(?MODULE, allocate_test, [Allocator, "Process C", 4]).

allocate_test(Allocator, Name, N) ->    
    io:format("~p requests ~p resources ~n", [Name, N]),
    S = allocator:request(Allocator, N),
    receive after 2000 -> ok end,
    io:format("~p releasing ~p~n", [Name, S]),
    allocator:release(Allocator, S),
    allocate_test(Allocator, Name, N).
