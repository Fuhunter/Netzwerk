function checkDel()
{
    if (document.getElementById("delete").checked) {
        return confirm('Du bist dabei deinen Account zu löschen! Dieser kann nicht wiederhergestellt werden!');
    }

}