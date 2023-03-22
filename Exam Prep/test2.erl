
-module(test2).
-compile(export_all).
psearch(F, L, MaxWorker) -> psearch(F, L, length(L), 1, MaxWorker).
psearch(_F, [], L, _CurrentWorker, _MaxWorker) -> gather([], L,1);

psearch(F, [H | T], L, CurrentWorker, MaxWorker)->
    Pid = spawn(fun worker/0),
    Pid ! {work, self(), H, F,CurrentWorker},
    psearch(F, T, L, CurrentWorker + 1, MaxWorker ).


worker() ->
    receive 
        {work, Pid, L, F,CurrentWorker} ->
            case lists:all(F,L) of
                true -> Pid ! {result, L,CurrentWorker};
                false -> Pid ! {resultnone, L,CurrentWorker}
            end
    end. 

gather(_Rest, 0,_CurrentWorker) -> lists:reverse(_Rest);
gather(Rest, L,CurrentWorker) -> 
    receive
        {result, Result,CurrentWorker} -> 
            gather([ Result| Rest], L-1,CurrentWorker+1);
        {resultnone, _Result,CurrentWorker} -> 
            gather(Rest, L-1,CurrentWorker+1)
    end.