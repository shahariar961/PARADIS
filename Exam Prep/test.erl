% search/2 that takes 2 arguments, a function (F) and a list of lists (L) (e.g., [[1,2,3],
% [2,3,3]]), and returns all lists in the list of lists for which the function returns true for at
% least one element.
% Example:
% search(fun (X) -> X == 1 end, [[1,2,3], [2,3,4], [a,b,1]]).
% should return the list [[1,2,3], [a,b,1]].

-module(test).
-compile(export_all).

% search(F,[H|T]) -> search(F,t[H|T],[]).
search(F,[H|T])-> search(F,[H|T],[]).
search(_F,[],Acc)-> lists:reverse(Acc);
search(F,[H|T],Acc) ->
    case filter(F,H) of
    true -> search(F,T,[H|Acc]);
    false -> search(F,T,Acc)
end.
filter(_F,[])->false;
filter(F,[H|T])->
    case F(H) of
        true -> true;
        false -> filter(F,T)
    end.

% do_work(_F,[])->
%     false;
do_work(F,L)->
    case list:all(F,L) of
        true->true;
        false->false
    end.

worker2() ->
    receive
        {work,Pid,F,List,Counter} ->
            case lists:all(F,List) of
                true-> Pid ! {go,List,Counter};
                false -> Pid ! {nogo,List,Counter}
            end
        end.

psearch(F,List,Threads)->
    % case Threads> length(List) of
    % true ->
        psearch(F,List,length(List),1).
    % false ->
    % end.
psearch(_F,[],Length,Counter) -> gather2([],Length,Counter);
psearch(F,[H|T],Length,Counter)->
    Pid=spawn(fun worker2/0),
    Pid ! {work,self(),F,H,Counter},
    psearch(F,T,Length,Counter+1).

gather2(Lists,0,_Counter)-> Lists;
gather2(Lists,Length,_Counter)->
    receive
        {go,List,Counter}->gather2([List|Lists],Length-1);
        {nogo,_List,Counter}->  gather2(Lists,Length-1)
    end.


% zip_filter/3 that takes three arguments: a function with two arguments and two lists,
% and returns a new list with the function applied to filter the corresponding elements in the
% list. That is, the function is applied to pairs of elements from both lists and filter (from
% both lists) elements for which the function returns true. The function should only accept
% lists of the same length.
% Example:
% zip_filter(fun (A, B) -> A + B > 3 end, [2, 2, 3], [1, 3, 1]).
% should return the tuple {[2, 3], [3, 1]}.

zip(F,A,B) -> zip(F,A,B,[],[]).
zip(_F,[],[],Acc1,Acc2)->{lists:reverse(Acc1),lists:reverse(Acc2)};
zip(F,[H|T],[N|G],Acc1,Acc2) ->
    case F(H,N) of
        true -> zip(F,T,G,[H|Acc1],[N|Acc2]);
        false -> zip(F,T,G,Acc1,Acc2)
    end.

% zip_map/4 that takes four arguments: a function with two arguments (the zipper), a
% function with two arguments (the accumulator) and two lists, and returns a new value
% with the first function applied to pairs of elements of the lists. The second function then
% accumulates the results of the first function call using the specified accumulator function.
% The function should only accept lists of the same length.
% Example:
% zip_map(fun (A, B) -> A * B end,
%  fun (X, Y) -> A + B,
%  [1, 2, 3],
%  [10, 20, 30]).
% should return 140 (10*1 + 2*20 + 3*30 = 140).

zip_map(F1,F2,A,B)->zip_map(F1,F2,A,B,[],0).
zip_map(_F1,_F2,[],[],[],Acc2)->
    Acc2;
zip_map(_F1,F2,[],[],[J|K],Acc2)->
    zip_map(_F1,F2,[],[],K,F2(J,Acc2));

zip_map(F1,F2,[H|T],[B|N],Acc1,Acc2) ->
    zip_map(F1,F2,T,N,[F1(H,B)|Acc1],Acc2).

% zip_map(fun (A, B) -> A * B end, fun (X, Y) -> X + Y end,[1, 2, 3],[10, 20, 30]).

do_work(F,A,B) ->
    case F(A,B) of
        true -> A,B;
        false ->ok
    end.

worker()->
    receive 
        {work,Pid,F,A,B} ->
            case F(A,B) of
                true ->Pid ! {go,A,B};
                false ->Pid !{nogo,A,B}
            end
        
    end.

pzip(F,A,B) -> pzip(F,A,B,length(A)).
pzip(F,[],[],Length)->
    gather([],[],Length);
pzip(F,[H1|T1],[H2|T2],Length) ->
    Pid = spawn(fun worker/0),
    Pid ! {work,self(),F,H1,H2},
    pzip(F,T1,T2,Length).

gather(L1,L2,0)->
    {lists:reverse(L1),lists:reverse(L2)};
gather(L1,L2,Length) ->
    receive
        {go,A,B} ->
            gather([A|L1],[B|L2],Length-1);
        {nogo,A,B}->
            gather(L1,L2,Length-1)

        end.



