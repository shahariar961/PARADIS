-module(pmap).
-export([unordered/2,unordered/3]).

unordered(Fun, List) ->
    Pids = [spawn_work(Fun, I) || I <- List],
    gather(Pids).

%  unordered(Fun, List, MaxWorkers) ->
%     MaxWorkers = min(MaxWorkers, length(List)),
%     Pids = [spawn_work(Fun, I) || I <- lists:sublist(List, MaxWorkers)],
%     gather(Pids).

unordered(Fun, List, MaxWorkers) ->
    Length = length(List),
    MaxWorkers = min(MaxWorkers, Length),
    SplitPoint = Length - (Length rem MaxWorkers),
    if SplitPoint == Length ->
        Pids = [spawn_work(Fun, I) || I <- List],
        gather(Pids);
    true->
    {FirstPart, LastPart} = lists:split(SplitPoint, List),
    Pids = [spawn_work(Fun, I) || I <- FirstPart] ++ [spawn_work(Fun, LastPart)],
    gather(Pids)
    end.

% ordered(Fun, List, MaxWorkers) ->
%         Length = length(List),
%         MaxWorkers = min(MaxWorkers, Length),
%         SplitPoint = Length - (Length rem MaxWorkers),
%         {FirstPart, LastPart} = lists:split(SplitPoint, List),
%         Pids = [spawn_work(Fun, I) || I <- FirstPart] ++ [spawn_work(Fun, LastPart)],
%         gather(Pids).
spawn_work(Fun, []) ->
    [];
spawn_work(Fun, I) ->
    Pid = spawn(fun() -> worker() end),
    Pid ! {self(), {work, Fun, I}},
    Pid.

worker() ->
    receive
	{Master, {work, Fun, I}} ->
        if is_list(I)->
            lists:map(fun(X) -> Master ! {self(), {result, Fun(X)}} end, I);
            true->
	    Master ! {self(), {result, Fun(I)}}
            end
    end.

gather([]) ->
    [];
gather([Pid|Pids]) ->
    receive
	{Pid, {result, R}} ->
	    [R | gather(Pids)]
    end.




