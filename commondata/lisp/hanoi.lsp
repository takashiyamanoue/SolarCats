'tower of hanoi
(defun hanoi (n a b c)
   (if (= n 0) 
         t
         (hanoi-aux n a b c)))
(defun hanoi-aux (n a b c)
   (hanoi (- n 1) a c b)
   (print (list 'move a 'to c))
   (hanoi (- n 1) b a c))