def hanoi(n,a,b,c)={
   if n>0 then
   {
      call hanoi(n-1,a,c,b);
      ? a+" to "+c;
      call hanoi(n-1,b,a,c)
   }
}
call hanoi(3,"a","b","c");