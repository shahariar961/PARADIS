-module(task1).
-export([eval/1, eval/2, map/2,filter/2,split/2]).




eval({X,A,B}) when is_tuple(A),is_integer(B)->
    {ok,math({X,math(A),B})};
    
eval({X,A,B}) when is_tuple(B),is_integer(A)->
    {ok,math({X,A,math(B)})};

eval({X,A,B}) when is_tuple(A),is_tuple(B)->
    V1=math(A),
    V2=math(B),
    {ok,math({X,V1,V2})};    

eval({X,A,B}) ->
{ok,math({X,A,B})}.

 math({X,A,B})->
     case X of
        add -> A+B;
        mul -> A*B;
        sub -> A-B;
        'div' -> A/B
    end.


eval({X,A,B},Map) when is_integer(B),is_tuple(A),is_map_key(b, Map),is_map_key(a,Map) ->
    V1=maps:get(a, Map),
    V2=maps:get(b, Map),
    {Z,_,_}=A,
    eval({X,math({Z,V1,V2}),B});
eval({X,A,B},Map) when is_tuple(A),is_tuple(B),is_map_key(b, Map),is_map_key(a,Map) ->
    V1=maps:get(a, Map),
    V2=maps:get(b, Map),
    {Z,_,_}=A,
    V=math(B),
    eval({X,math({Z,V1,V2}),V});
eval({X,A,B},Map) when is_map_key(a,Map),is_tuple(B) ->
    V1=maps:get(a, Map),
    V2=math(B),
    eval({X,V1,V2});

eval({X,A,B},Map) when is_map_key(b,Map),is_tuple(A) ->
    V2=maps:get(b, Map),
    V1=math(A),
    eval({X,V1,V2});
eval({X,A,B},Map) when is_tuple(A),is_tuple(B) ->
    V1=math(A),
    V2=math(B),
    eval({X,V1,V2});

eval({X,A,B},Map) when is_integer(A),is_tuple(B) ->
    V2=math(B),
    eval({X,A,V2});
eval({X,A,B},Map) when is_integer(B),is_tuple(A) ->
    V1=math(A),
    eval({X,V1,B});
eval({X,A,B},Map) when is_map_key(a,Map),is_integer(B) ->
    V=maps:get(a, Map),
    eval({X,V,B});
    
eval({X,A,B},Map) when is_map_key(b, Map),is_integer(A) ->
    V=maps:get(b, Map),
    eval({X,A,V});

eval({X,A,B},Map) when is_map_key(b, Map),is_map_key(a,Map) ->
    V1=maps:get(a, Map),
    V2=maps:get(b, Map),
    eval({X,V1,V2});

eval({X,A,B},Map) ->
 {ok,math({X,A,B})}.
    

%map(_F,[])->
    %[];

map(F,[H|T]) -> map(F,[H|T],[]).
map(F,[],Acc)->lists:reverse(Acc);
map(F,[H|T],Acc)->map(F, T, [F(H)|Acc]).



filter(F,[H|T])-> filter(F,[H|T],[]).
filter(F,[],Acc)->lists:reverse(Acc);

filter(F, [H|T], Acc)-> 
    case F(H) of
        true ->filter(F,T,[H|Acc]);
        false->filter(F,T,Acc)
    end.

split(F,[H|T])-> split(F,[H|T],[],[]).
split(F,[],Acc,Acc2)->
    {lists:reverse(Acc),
    lists:reverse(Acc2)};

split(F, [H|T], Acc,Acc2)-> 
    case F(H) of
        true ->split(F,T,[H|Acc],Acc2);
        false->split(F,T,Acc,[H|Acc2])
    end.