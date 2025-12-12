import { useState, useEffect } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useToast } from '@/components/ui/use-toast';
import { Toaster } from '@/components/ui/toaster';
import { Calendar, Plus, BookOpen, Bell, Loader2 } from 'lucide-react';
import type { Exam, CreateExamRequest } from '@/types/exam';

// Mock data for demo (when backend is not available)
const mockExams: Exam[] = [
  { id: '1', title: 'Midterm - Software Architecture', date: '2024-02-15' },
  { id: '2', title: 'Final - Distributed Systems', date: '2024-03-20' },
  { id: '3', title: 'Quiz - Microservices Patterns', date: '2024-02-01' },
];

export default function ExamDashboard() {
  const [exams, setExams] = useState<Exam[]>(mockExams);
  const [isLoading, setIsLoading] = useState(false);
  const [isCreating, setIsCreating] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState<CreateExamRequest>({
    title: '',
    date: '',
  });
  const { toast } = useToast();

  // Fetch exams from backend (when available)
  const fetchExams = async () => {
    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/exams', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('jwt_token') || ''}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setExams(data);
      }
    } catch (error) {
      // Use mock data if backend is not available
      console.log('Using mock data - backend not available');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchExams();
  }, []);

  // Create exam handler
  const handleCreateExam = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.title || !formData.date) {
      toast({
        title: 'Validation Error',
        description: 'Please fill in all fields',
        variant: 'destructive',
      });
      return;
    }

    setIsCreating(true);
    
    try {
      const response = await fetch('http://localhost:8080/api/exams', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('jwt_token') || ''}`,
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const newExam = await response.json();
        setExams([...exams, newExam]);
        toast({
          title: 'Exam Created! 🎉',
          description: `"${formData.title}" has been scheduled. Notifications will be sent to all students.`,
        });
      } else {
        throw new Error('Failed to create exam');
      }
    } catch (error) {
      // Demo mode - add to local state
      const newExam: Exam = {
        id: Date.now().toString(),
        title: formData.title,
        date: formData.date,
      };
      setExams([...exams, newExam]);
      toast({
        title: 'Exam Created! 🎉 (Demo Mode)',
        description: `"${formData.title}" has been scheduled. In production, notifications would be sent via RabbitMQ.`,
      });
    } finally {
      setIsCreating(false);
      setShowForm(false);
      setFormData({ title: '', date: '' });
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  const getDaysUntil = (dateString: string) => {
    const examDate = new Date(dateString);
    const today = new Date();
    const diffTime = examDate.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
  };

  return (
    <div className="min-h-screen bg-background p-6">
      <Toaster />
      
      {/* Header */}
      <div className="max-w-6xl mx-auto">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
              <BookOpen className="h-8 w-8 text-primary" />
              Exam Dashboard
            </h1>
            <p className="text-muted-foreground mt-1">
              Team Delta - Exam Service (Port 8084)
            </p>
          </div>
          <Button onClick={() => setShowForm(!showForm)} className="gap-2">
            <Plus className="h-4 w-4" />
            Create Exam
          </Button>
        </div>

        {/* Create Exam Form */}
        {showForm && (
          <Card className="mb-8 border-primary/20">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Calendar className="h-5 w-5" />
                Schedule New Exam
              </CardTitle>
              <CardDescription>
                Create an exam and automatically notify all students via the Notification Service (Port 8085)
              </CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleCreateExam} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="title">Exam Title</Label>
                    <Input
                      id="title"
                      placeholder="e.g., Midterm - Software Architecture"
                      value={formData.title}
                      onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="date">Exam Date</Label>
                    <Input
                      id="date"
                      type="date"
                      value={formData.date}
                      onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                    />
                  </div>
                </div>
                <div className="flex gap-2">
                  <Button type="submit" disabled={isCreating}>
                    {isCreating && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                    {isCreating ? 'Creating...' : 'Create Exam'}
                  </Button>
                  <Button type="button" variant="outline" onClick={() => setShowForm(false)}>
                    Cancel
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}

        {/* Info Card */}
        <Card className="mb-8 bg-muted/50">
          <CardContent className="pt-6">
            <div className="flex items-start gap-3">
              <Bell className="h-5 w-5 text-primary mt-0.5" />
              <div>
                <p className="font-medium">Event-Driven Architecture</p>
                <p className="text-sm text-muted-foreground">
                  When an exam is created, a message is published to RabbitMQ exchange 'exam-events' (key: 'exam.created'). 
                  The Notification Service (Port 8085) listens for these events and sends SMS/Email notifications to all students.
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Exams Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {isLoading ? (
            <div className="col-span-full flex justify-center py-12">
              <Loader2 className="h-8 w-8 animate-spin text-primary" />
            </div>
          ) : exams.length === 0 ? (
            <div className="col-span-full text-center py-12">
              <BookOpen className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
              <p className="text-muted-foreground">No exams scheduled yet</p>
            </div>
          ) : (
            exams.map((exam) => {
              const daysUntil = getDaysUntil(exam.date);
              const isPast = daysUntil < 0;
              const isUpcoming = daysUntil >= 0 && daysUntil <= 7;
              
              return (
                <Card 
                  key={exam.id} 
                  className={`transition-all hover:shadow-md ${
                    isPast ? 'opacity-60' : isUpcoming ? 'border-primary/50' : ''
                  }`}
                >
                  <CardHeader className="pb-2">
                    <CardTitle className="text-lg">{exam.title}</CardTitle>
                    <CardDescription className="flex items-center gap-2">
                      <Calendar className="h-4 w-4" />
                      {formatDate(exam.date)}
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className={`text-sm font-medium ${
                      isPast ? 'text-muted-foreground' : 
                      isUpcoming ? 'text-destructive' : 'text-primary'
                    }`}>
                      {isPast 
                        ? 'Completed' 
                        : daysUntil === 0 
                          ? 'Today!' 
                          : `${daysUntil} days remaining`
                      }
                    </div>
                  </CardContent>
                </Card>
              );
            })
          )}
        </div>

        {/* Architecture Info */}
        <Card className="mt-8">
          <CardHeader>
            <CardTitle>🟣 Team Delta Architecture</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="p-4 rounded-lg bg-muted/50">
                <h4 className="font-semibold mb-2">Exam Service (Port 8084)</h4>
                <ul className="text-sm text-muted-foreground space-y-1">
                  <li>• POST /api/exams - Create exam</li>
                  <li>• GET /api/exams - List all exams</li>
                  <li>• Publishes to 'exam-events' exchange</li>
                </ul>
              </div>
              <div className="p-4 rounded-lg bg-muted/50">
                <h4 className="font-semibold mb-2">Notification Service (Port 8085)</h4>
                <ul className="text-sm text-muted-foreground space-y-1">
                  <li>• Listens to 'user-events' queue</li>
                  <li>• Listens to 'exam-events' queue</li>
                  <li>• Sends Email/SMS notifications</li>
                </ul>
              </div>
            </div>
            <div className="p-4 rounded-lg bg-primary/5 border border-primary/20">
              <h4 className="font-semibold mb-2">🔧 Circuit Breaker Pattern</h4>
              <p className="text-sm text-muted-foreground">
                The Exam Service uses Resilience4j Circuit Breaker when calling the Notification Service directly via Feign Client. 
                If the Notification Service is down, the fallback logs: "Notification service unavailable, queuing for later".
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
